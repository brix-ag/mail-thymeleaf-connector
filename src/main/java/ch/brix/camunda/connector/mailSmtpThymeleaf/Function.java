package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.StringLike;
import ch.brix.camunda.connector.util.gson.StringLikeAdapter;
import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollection;
import ch.brix.camunda.connector.util.gson.delimitedCollections.DelimitedCollectionsAdapter;
import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedStringSet;
import ch.brix.camunda.connector.util.gson.user.*;
import ch.brix.camunda.connector.util.mail.Email;
import ch.brix.camunda.connector.util.mail.EmailService;
import ch.brix.camunda.connector.util.templateGenerator.Deserializer;
import com.google.gson.*;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.error.ConnectorException;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@OutboundConnector(
        name = "BRIXMAILSMTPTHYMELEAF",
        inputVariables = {},
        type = "ch.brix.camunda.connectors.mailSmtpThymeleaf:1")
@Component
@Slf4j
@RequiredArgsConstructor
public class Function implements OutboundConnectorFunction {

    private final EmailService emailService;
    private final Validator validator;
    private final ApplicationContext applicationContext;
    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(StringLike.class, new StringLikeAdapter())
            .registerTypeHierarchyAdapter(DelimitedCollection.class, new DelimitedCollectionsAdapter())
            .create();

    /**
     * @param context context
     * @return response which is serialized by the standard serializer on purpose, so arrays of strings are returned instead of strings with comma-separated values.
     * @throws ConnectorException see ErrorCodes for a list of error codes
     */
    @Override
    public Object execute(OutboundConnectorContext context) throws ConnectorException {
        log.debug("===== Start execute() ====");
        try {
            Request request = Deserializer.deserialize(context.getJobContext().getVariables(), gson, Request.class);
            log.debug("Request deserialized");
            Set<ConstraintViolation<Request>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String violationsString = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n- "));
                log.error("{} constraints violated: \n- {}", violations.size(), violationsString);
                throw new ConnectorException(ErrorCodes.VALIDATION.name(), "Basic validation failed: \n- " + violationsString);
            } else {
                log.debug("Request successfully validated");
            }
            Map<String, Object> variables = loadVariables(context.getJobContext().getVariables(), request.getTemplateGroup().getVariables());
            log.debug("Loaded variables for template");
            Email email = new Email();
            email.setTemplate(request.getTemplateGroup().getTemplate());
            email.setLocale(request.getTemplateGroup().getFallbackLocale()); // may be overwritten by recipient
            email.setVariables(variables);
            processSender(request, email);
            log.debug("Sender processed");
            Response response = new Response();
            processCopies(request, email);
            log.debug("Copies processed");
            sendMails(request, email, response, new HashSet<>());
            log.debug("Mails sent");
            return response;
        } catch (Throwable e) {
            if (e instanceof ConnectorException)
                throw (ConnectorException) e;
            log.error(e.getMessage(), e);
            throw new ConnectorException(ErrorCodes.INTERNAL.name(), e.getMessage(), e);
        }
    }

    private void processCopies(Request request, Email email) {
        EmailAddressSet cc = request.getCopiesGroup().getCcEmailAddresses();
        EmailAddressSet bcc = request.getCopiesGroup().getBccEmailAddresses();
        String errors = Stream.of(cc, bcc).flatMap(Collection::stream)
                .filter(addr -> !addr.isValid())
                .map(EmailAddress::toString)
                .collect(Collectors.joining(", "));
        if (!errors.isEmpty())
            throw new ConnectorException(ErrorCodes.COPIES.name(), "Invalid email addresses in cc or bcc: " + errors);
        email.setCc(cc.stream().map(EmailAddress::toString).collect(Collectors.toSet()));
        email.setBcc(bcc.stream().map(EmailAddress::toString).collect(Collectors.toSet()));
    }

    private void processSender(Request request, Email email) {
        if (request.getSenderGroup().getEmailAddress() != null) {
            if (!request.getSenderGroup().getEmailAddress().isValid())
                throw new ConnectorException(ErrorCodes.SENDER.name(), "Illegal mail address");
            email.setFrom(request.getSenderGroup().getEmailAddress().getEmail());
            if (request.getTemplateGroup().getSenderVariableName() != null)
                email.getVariables().put(request.getTemplateGroup().getSenderVariableName(), User.of(request.getSenderGroup().getEmailAddress()));
        } else if (request.getSenderGroup().getUserById() != null) {
            UserRepresentation user = request.getSenderGroup().getUserById().getUser();
            if (user == null)
                throw new ConnectorException(ErrorCodes.SENDER.name(), "Sender " + request.getSenderGroup().getUserById().toString() + " not found.");
            email.setFrom(user.getEmail());
            if (request.getTemplateGroup().getSenderVariableName() != null)
                email.getVariables().put(request.getTemplateGroup().getSenderVariableName(), User.of(user, request));
        } else if  (request.getSenderGroup().getUserByUsername() != null) {
            UserRepresentation user = request.getSenderGroup().getUserByUsername().getUser();
            if (user == null)
                throw new ConnectorException(ErrorCodes.SENDER.name(), "Sender " + request.getSenderGroup().getUserByUsername().toString() + " not found.");
            email.setFrom(user.getEmail());
            if (request.getTemplateGroup().getSenderVariableName() != null)
                email.getVariables().put(request.getTemplateGroup().getSenderVariableName(), User.of(user, request));
        } else if (request.getSenderGroup().getUserByEmail() != null) {
            UserRepresentation user = request.getSenderGroup().getUserByEmail().getUser();
            if (user == null)
                throw new ConnectorException(ErrorCodes.SENDER.name(), "Sender " + request.getSenderGroup().getUserByEmail().toString() + " not found.");
            email.setFrom(user.getEmail());
            if (request.getTemplateGroup().getSenderVariableName() != null)
                email.getVariables().put(request.getTemplateGroup().getSenderVariableName(), User.of(user, request));
        }
    }

    private Map<String, Object> loadVariables(String variables, CommaSeparatedStringSet tptVariables) {
        Map<String, Object> ret = new HashMap<>();
        JsonObject obj = JsonParser.parseString(variables).getAsJsonObject();
        if (tptVariables == null || tptVariables.isEmpty()) {
            obj.entrySet().stream()
                    .filter(entry -> !entry.getValue().isJsonNull())
                    .forEach(entry -> ret.put(entry.getKey(), loadVariable(entry.getValue())));
        } else {
            tptVariables.stream()
                    .filter(obj::has)
                    .filter(name -> !obj.get(name).isJsonNull())
                    .forEach(name -> ret.put(name, loadVariable(obj.get(name))));
        }
        return ret;
    }

    private Object loadVariable(JsonElement el) {
        if (el.isJsonNull())
            return null;
        if (el.isJsonPrimitive()) {
            if (el.getAsJsonPrimitive().isString())
                return el.getAsString();
            if (el.getAsJsonPrimitive().isBoolean())
                return el.getAsBoolean();
            if (el.getAsJsonPrimitive().isNumber())
                return el.getAsNumber();
        } else if (el.isJsonObject()) {
            Map<String, Object> map = new HashMap<>();
            el.getAsJsonObject().entrySet().stream()
                    .filter(entry -> !entry.getValue().isJsonNull())
                    .forEach(entry -> map.put(entry.getKey(), loadVariable(entry.getValue())));
            return map;
        } else if (el.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            el.getAsJsonArray().forEach(inner -> list.add(loadVariable(inner)));
            return list;
        }
        return null;
    }

    private void sendMails(Request request, Email email, Response response, Set<String> sent) {
        request.getRecipientsGroup().getEmailAddresses().forEach(address ->
                sendMail(email, User.of(address), request, response, response.getFailedUsersByEmail(), address, sent));
        request.getRecipientsGroup().getUsersById().forEach(userById -> {
            UserRepresentation ur;
            try {
                ur = userById.getUser();
            } catch (Exception e) {
                log.error("Failed to get user by id: {}", userById, e);
                response.incrementErrors();
                response.getFailedUsersById().add(userById);
                return;
            }
            sendMail(email, User.of(ur, request), request, response, response.getFailedUsersById(), userById, sent);
        });
        request.getRecipientsGroup().getUsersByUsername().forEach(userByUsername -> {
            UserRepresentation ur;
            try {
                ur = userByUsername.getUser();
            } catch (Exception e) {
                log.error("Failed to get user by username: {}", userByUsername, e);
                response.incrementErrors();
                response.getFailedUsersByUsername().add(userByUsername);
                return;
            }
            sendMail(email, User.of(ur, request), request, response, response.getFailedUsersByUsername(), userByUsername, sent);
        });
        request.getRecipientsGroup().getUsersByEmailAddress().forEach(userByEmail -> {
            UserRepresentation ur;
            try {
                ur = userByEmail.getUser();
            } catch (Exception e) {
                log.error("Failed to get user by email: {}", userByEmail, e);
                response.incrementErrors();
                response.getFailedUsersByEmail().add(userByEmail);
                return;
            }
            sendMail(email, User.of(ur, request), request, response, response.getFailedUsersByEmail(), userByEmail, sent);
        });
        request.getRecipientsGroup().getUsersByGroupId().forEach(groupById -> {
            List<UserRepresentation> members;
            try {
                members = groupById.getGroup().members();
                if (members == null || members.isEmpty())
                    return;
            } catch (Exception e) {
                log.error("Failed to get members of group by id: {}", groupById, e);
                response.incrementErrors();
                response.getFailedGroupsById().add(groupById);
                return;
            }
            members.forEach(ur -> sendMail(email, User.of(ur, request), request, response, response.getFailedGroupsById(), groupById, sent));
        });
        request.getRecipientsGroup().getUsersByGroupName().forEach(groupByName -> {
            List<UserRepresentation> members;
            try {
                members = groupByName.getGroup().members();
                if (members == null || members.isEmpty())
                    return;
            } catch (Exception e) {
                log.error("Failed to get members of group by name: {}", groupByName, e);
                response.incrementErrors();
                response.getFailedGroupsByName().add(groupByName);
                return;
            }
            members.forEach(ur -> sendMail(email, User.of(ur, request), request, response, response.getFailedGroupsByName(), groupByName, sent));
        });
        request.getRecipientsGroup().getUsersByRoleName().forEach(roleByName -> {
            List<UserRepresentation> members;
            try {
                members = roleByName.getRole().getUserMembers();
                if (members == null || members.isEmpty())
                    return;
            } catch (Exception e) {
                log.error("Failed to get members of role by name: {}", roleByName, e);
                response.incrementErrors();
                response.getFailedRolesByName().add(roleByName);
                return;
            }
            members.forEach(ur -> sendMail(email, User.of(ur, request), request, response, response.getFailedRolesByName(), roleByName, sent));
        });
    }

    private void sendMail(Email mail, User user, Request request, Response response, Collection failCollection, Object failObject, Set<String> sent) {
        if (sent.contains(user.getEmail().toLowerCase()))
            return;
        if (!new EmailAddress(user.getEmail()).isValid()) {
            log.error("Illegal email address {} for user (possibly of group or role): {} ({})", user.getEmail(), failObject, failObject.getClass().getSimpleName());
            addError(user, response, failCollection, failObject);
            return;
        }
        try {
            mail = mail.toBuilder().build(); // copy
            mail.setTo(Collections.singleton(user.getEmail()));
            if (request.getTemplateGroup().getUserVariableName() != null)
                mail.getVariables().put(request.getTemplateGroup().getUserVariableName(), user);
            if (user.getLocale() != null)
                mail.setLocale(user.getLocale());
            mail.setSubject(request.getSubjectGroup().getSubject().getMessage(List.of(mail.getLocale(), request.getTemplateGroup().getFallbackLocale()),
                    request.getSubjectGroup().getReplacement0(),
                    request.getSubjectGroup().getReplacement1(),
                    request.getSubjectGroup().getReplacement2(),
                    request.getSubjectGroup().getReplacement3(),
                    request.getSubjectGroup().getReplacement4()));
            emailService.send(mail);
            sent.add(user.getEmail().toLowerCase());
            response.incrementSent();
        } catch (Exception e) {
            log.error("Failed to send mail to {}", user.getEmail(), e);
            addError(user, response, failCollection, failObject);
        }
    }

    private void addError(User user, Response response, Collection failCollection, Object failObject) {
        response.incrementErrors();
        failCollection.add(failObject);
        if (failCollection instanceof GroupByIdSet || failCollection instanceof  GroupByNameSet)
            response.getFailedUsernamesInGroups().add(user.getUsername());
        else if (failCollection instanceof RoleByNameSet)
            response.getFailedUsernamesInRoles().add(user.getUsername());
    }

}
