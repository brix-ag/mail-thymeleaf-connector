package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.user.ExtendedEmailAddress;
import ch.brix.camunda.connector.util.gson.user.Sex;
import lombok.*;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class User {

    private String id;
    private String username;
    @EqualsAndHashCode.Include
    private String email;
    private String firstName;
    private String lastName;
    private Locale locale;
    private Sex sex;
    private Map<String, String> attributes;

    public static User of(ExtendedEmailAddress address) {
        return User.builder()
                .email(address.getEmail())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .locale(address.getLocale())
                .sex(address.getSex())
                .build();
    }

    public static User of(UserRepresentation user, Request request) {
        String locale = getAttribute(user, request.getKeycloakGroup().getLocaleAttributeName());
        String sex = getAttribute(user, request.getKeycloakGroup().getSexAttributeName());
        return User.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .locale(locale == null || locale.isBlank() ? null : Locale.forLanguageTag(locale))
                .sex(getSex(sex, request))
                .build();
    }

    private static Sex getSex(String sex, Request request) {
        if (sex == null)
            return Sex.UNSPECIFIED;
        if (sex.equals(request.getKeycloakGroup().getMaleSexAttributeValue()))
            return Sex.MALE;
        if (sex.equals(request.getKeycloakGroup().getFemaleSexAttributeValue()))
            return Sex.FEMALE;
        return Sex.UNSPECIFIED;
    }

    private static String getAttribute(UserRepresentation user, String attribute) {
        if (attribute == null || user == null || user.getAttributes() == null)
            return null;
        List<String> list = user.getAttributes().get(attribute);
        if (list == null || list.isEmpty())
            return null;
        return list.get(0);
    }
}
