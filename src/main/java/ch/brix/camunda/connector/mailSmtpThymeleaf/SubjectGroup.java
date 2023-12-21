package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.lang.MessageKey;
import ch.brix.camunda.connector.util.templateGenerator.PropertyDefinition;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Subject group properties are prefixed by sjt to avoid name clashes.
 */
@Getter
public class SubjectGroup {

    @PropertyDefinition(
            label = "Subject Message Key",
            description = "Returns the message key itself if not found.",
            notEmpty = true
    )
    @Valid
    @NotNull
    @SerializedName("sjtSubject")
    private MessageKey subject;

    @PropertyDefinition(
            label = "Replacement {0}"
    )
    @SerializedName("sjtReplacement0")
    private String replacement0;

    @PropertyDefinition(
            label = "Replacement {1}"
    )
    @SerializedName("sjtReplacement1")
    private String replacement1;

    @PropertyDefinition(
            label = "Replacement {2}"
    )
    @SerializedName("sjtReplacement2")
    private String replacement2;

    @PropertyDefinition(
            label = "Replacement {3}"
    )
    @SerializedName("sjtReplacement3")
    private String replacement3;

    @PropertyDefinition(
            label = "Replacement {4}"
    )
    @SerializedName("sjtReplacement4")
    private String replacement4;

}
