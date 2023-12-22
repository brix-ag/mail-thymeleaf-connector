package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedStringSet;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * Keycloak group properties are prefixed by kck to avoid name clashes.
 */
@Getter
public class KeycloakGroup {

    @PropertyDefinition(
            label = "Locale Attribute Name",
            tooltip = "If set the locale is taken from this attribute."
    )
    @SerializedName("kckLocaleAttributeName")
    private String localeAttributeName;

    @PropertyDefinition(
            label = "Sex Attribute Name",
            tooltip = "If set the sex is taken from this attribute."
    )
    @SerializedName("kckSexAttributeName")
    private String sexAttributeName;

    @PropertyDefinition(
            label = "Male Sex Attribute Value",
            tooltip = "This value will be interpreted as male (for the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\" target=\"_blank\">User object</a>).",
            value = "m"
    )
    @SerializedName("kckMaleSexAttributeValue")
    private String maleSexAttributeValue;

    @PropertyDefinition(
            label = "Female Sex Attribute Value",
            tooltip = "This value will be interpreted as female (for the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\" target=\"_blank\">User object</a>).",
            value = "f"
    )
    @SerializedName("kckFemaleSexAttributeValue")
    private String femaleSexAttributeValue;

    @PropertyDefinition(
            label = "Template Attributes",
            tooltip = "Comma-separated list of attribute names that should be available in the template on the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\" target=\"_blank\">User objects</a>."
    )
    @SerializedName("kckTemplateAttributes")
    private CommaSeparatedStringSet templateAttributes;

}
