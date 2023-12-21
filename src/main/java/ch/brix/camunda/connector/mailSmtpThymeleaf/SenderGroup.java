package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.user.ExtendedEmailAddress;
import ch.brix.camunda.connector.util.gson.user.UserByEmail;
import ch.brix.camunda.connector.util.gson.user.UserById;
import ch.brix.camunda.connector.util.gson.user.UserByUsername;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

/**
 * Sender group properties are prefixed by sdr to avoid name clashes.
 */
@Getter
public class SenderGroup {

    @PropertyDefinition(
            label = "Email Address",
            description = "Use this to specify an email address directly. Extended syntax supported (but not required): [ firstName ';' ] lastName '&lt;' emailAddress '&gt;' [ locale [ ';' sex ] ]. Where m = male and f = female, other = unspecified. E.g. John;Doe&lt;john.doe@acme.com&gt;en;m"
    )
    @Valid
    @SerializedName("sdrEmailAddress")
    private ExtendedEmailAddress emailAddress;

    @PropertyDefinition(
            label = "User by ID",
            description = "Take email address from keycloak user."
    )
    @Valid
    @SerializedName("sdrUserById")
    private UserById userById;

    @PropertyDefinition(
            label = "User by Username",
            description = "Take email address from keycloak user."
    )
    @Valid
    @SerializedName("sdrUserByUsername")
    private UserByUsername userByUsername;

    @PropertyDefinition(
            label = "User by Email Address",
            description = "Take this email address and load the keycloak user with this address."
    )
    @Valid
    @SerializedName("sdrUserByEmail")
    private UserByEmail userByEmail;

    @AssertTrue
    public boolean exactlyOneSenderDefined() {
        return (emailAddress == null ? 0: 1) + (userById == null ? 0 : 1) + (userByUsername == null ? 0 : 1) + (userByEmail == null ? 0: 1) == 1;
    }

}
