package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.user.*;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import lombok.Getter;

/**
 * Recipients group properties are prefixed by rcp to avoid name clashes.
 */
@Getter
public class RecipientsGroup {

    @PropertyDefinition(
            label = "Email Addresses",
            tooltip = "Use this to specify email addresses directly. <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#extended-email-syntax\">Extended syntax</a> supported (but not required): [ firstName ';' ] lastName '&lt;' emailAddress '&gt;' [ locale [ ';' sex ] ]. Where m = male and f = female, other = unspecified. E.g. John;Doe&lt;john.doe@acme.com&gt;en;m"
    )
    @Valid
    @SerializedName("rcpEmailAddresses")
    private ExtendedEmailAddressSet emailAddresses;

    @PropertyDefinition(
            label = "Users by ID",
            tooltip = "Take email address from keycloak users having the specified IDs (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersById")
    private UserByIdSet usersById;

    @PropertyDefinition(
            label = "Users by Username",
            tooltip = "Take email address from keycloak users having the specified usernames (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersByUsername")
    private UserByUsernameSet usersByUsername;

    @PropertyDefinition(
            label = "Users by Email Address",
            tooltip = "Load users having the specified email addresses and take those addresses (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersByEmailAddress")
    private UserByEmailSet usersByEmailAddress;


    @PropertyDefinition(
            label = "Users by Group ID",
            tooltip = "Take email address from keycloak users which are members of the specified groups (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersByGroupId")
    private GroupByIdSet usersByGroupId;

    @PropertyDefinition(
            label = "Users by Group Name",
            tooltip = "Take email address from keycloak users which are members of the specified groups (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersByGroupName")
    private GroupByNameSet usersByGroupName;

    @PropertyDefinition(
            label = "Users by Role Name",
            tooltip = "Take email address from keycloak users which are members of the specified roles (comma-separated)."
    )
    @Valid
    @SerializedName("rcpUsersByRoleName")
    private RoleByNameSet usersByRoleName;

}