package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedStringSet;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Locale;

/**
 * Template group properties are prefixed by tpt to avoid name clashes.
 */
@Getter
public class TemplateGroup {

    @PropertyDefinition(
            label = "Thymeleaf Template",
            tooltip = "Relative to template root without extension. Templates have to be in the runtime.",
            notEmpty = true
    )
    @NotBlank
    @SerializedName("tptTemplate")
    private String template;

    @PropertyDefinition(
            label = "Fallback Locale",
            tooltip = "Locale to use for subject and template if not defined through other means."
    )
    @SerializedName("tptFallbackLocale")
    private Locale fallbackLocale;

    @PropertyDefinition(
            label = "Template Variables",
            tooltip = "Variables that should be available in the template (empty = all). JSON booleans are deserialized to Boolean, strings to String, numbers to Number, array to ArrayList and objects to HashMap&lt;String, Object&gt;."
    )
    @SerializedName("tptVariables")
    private CommaSeparatedStringSet variables;

    @PropertyDefinition(
            label = "Recipient Variable Name",
            tooltip = "Name of the variable to which the recipient <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\">User object</a> is passed."
    )
    @SerializedName("tptUserVariableName")
    private String userVariableName;

    @PropertyDefinition(
            label = "Sender Variable Name",
            tooltip = "Name of the variable to which the sender <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\">User object</a> is passed."
    )
    @SerializedName("tptSenderVariableName")
    private String senderVariableName;

}
