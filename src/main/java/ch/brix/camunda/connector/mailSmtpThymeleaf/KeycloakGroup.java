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
            description = "If set the locale is taken from this attribute."
    )
    @SerializedName("kckLocaleAttributeName")
    private String localeAttributeName;

    @PropertyDefinition(
            label = "Sex Attribute Name",
            description = "If set the sex is taken from this attribute."
    )
    @SerializedName("kckSexAttributeName")
    private String sexAttributeName;

    @PropertyDefinition(
            label = "Male Sex Attribute Value",
            description = "This value will be interpreted as male (for the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\">User object</a>).",
            value = "m"
    )
    @SerializedName("kckMaleSexAttributeValue")
    private String maleSexAttributeValue;

    @PropertyDefinition(
            label = "Female Sex Attribute Value",
            description = "This value will be interpreted as female (for the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\">User object</a>).",
            value = "f"
    )
    @SerializedName("kckFemaleSexAttributeValue")
    private String femaleSexAttributeValue;

    @PropertyDefinition(
            label = "Template Attributes",
            description = "Comma-separated list of attribute names that should be available in the template on the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#user-object\">User objects</a>."
    )
    @SerializedName("kckTemplateAttributes")
    private CommaSeparatedStringSet templateAttributes;

}
