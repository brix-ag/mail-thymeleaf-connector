package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.user.EmailAddressSet;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import lombok.Getter;

/**
 * Copies group properties are prefixed by cps to avoid name clashes.
 */
@Getter
public class CopiesGroup {

    @PropertyDefinition(
            label = "Cc Email Addresses",
            description = "Each mail is also sent to those email addresses (comma-separated)."
    )
    @Valid
    @SerializedName("cpsCcEmailAddresses")
    private EmailAddressSet ccEmailAddresses;

    @PropertyDefinition(
            label = "Bcc Email Addresses",
            description = "Each mail is also sent to those email addresses (comma-separated). This can be used to easily create a complete email history."
    )
    @Valid
    @SerializedName("cpsBccEmailAddresses")
    private EmailAddressSet bccEmailAddresses;

}
