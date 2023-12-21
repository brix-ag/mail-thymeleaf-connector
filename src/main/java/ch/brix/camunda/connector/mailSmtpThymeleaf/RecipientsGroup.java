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
            description = "Use this to specify email addresses directly. Extended syntax supported (but not required): [ firstName ';' ] lastName '&lt;' emailAddress '&gt;' [ locale [ ';' sex ] ]. Where m = male and f = female, other = unspecified. E.g. John;Doe&lt;john.doe@acme.com&gt;en;m"
    )
    @Valid
    @SerializedName("rcpEmailAddresses")
    private ExtendedEmailAddressSet emailAddresses;

    @PropertyDefinition(
            label = "Users by ID",
            description = "Take email address from keycloak users having the specified IDs."
    )
    @Valid
    @SerializedName("rcpUsersById")
    private UserByIdSet usersById;

    @PropertyDefinition(
            label = "Users by Username",
            description = "Take email address from keycloak users having the specified usernames."
    )
    @Valid
    @SerializedName("rcpUsersByUsername")
    private UserByUsernameSet usersByUsername;

    @PropertyDefinition(
            label = "Users by Email Address",
            description = "Load users having the specified email addresses and take those addresses."
    )
    @Valid
    @SerializedName("rcpUsersByEmailAddress")
    private UserByEmailSet usersByEmailAddress;


    @PropertyDefinition(
            label = "Users by Group ID",
            description = "Take email address from keycloak users which are members of the specified groups."
    )
    @Valid
    @SerializedName("rcpUsersByGroupId")
    private GroupByIdSet usersByGroupId;

    @PropertyDefinition(
            label = "Users by Group Name",
            description = "Take email address from keycloak users which are members of the specified groups."
    )
    @Valid
    @SerializedName("rcpUsersByGroupName")
    private GroupByNameSet usersByGroupName;

    @PropertyDefinition(
            label = "Users by Role Name",
            description = "Take email address from keycloak users which are members of the specified roles."
    )
    @Valid
    @SerializedName("rcpUsersByRoleName")
    private RoleByNameSet usersByRoleName;

}