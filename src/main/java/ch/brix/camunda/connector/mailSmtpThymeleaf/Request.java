package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.templateGenerator.PropertyGroup;
import ch.brix.camunda.connector.util.templateGenerator.TemplateDefinition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@TemplateDefinition(
        name = "Mail Thymeleaf",
        id = "ch.brix.camunda.connectors.mailSmtpThymeleaf:1",
        version = 1,
        description = "Send mails using Thymeleaf and Keycloak to resolve users",
        documentation = "https://github.com/brix-ag/mail-thymeleaf-connector",
        icon = "data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEuMSIgd2lkdGg9IjgwMHB4IiBoZWlnaHQ9IjgwMHB4IiBzdHlsZT0ic2hhcGUtcmVuZGVyaW5nOmdlb21ldHJpY1ByZWNpc2lvbjsgdGV4dC1yZW5kZXJpbmc6Z2VvbWV0cmljUHJlY2lzaW9uOyBpbWFnZS1yZW5kZXJpbmc6b3B0aW1pemVRdWFsaXR5OyBmaWxsLXJ1bGU6ZXZlbm9kZDsgY2xpcC1ydWxlOmV2ZW5vZGQiIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4KPGc+PHBhdGggc3R5bGU9Im9wYWNpdHk6MC45ODIiIGZpbGw9IiMwMDAwMDAiIGQ9Ik0gNzk5LjUsMjA0LjUgQyA3OTkuNSwzMzQuNSA3OTkuNSw0NjQuNSA3OTkuNSw1OTQuNUMgNzkyLjQ2LDYyOS4wNDUgNzcxLjc5Myw2NTAuODc4IDczNy41LDY2MEMgNjI0Ljg2Miw2NjEuMTYxIDUxMi4xOTYsNjYxLjY2MSAzOTkuNSw2NjEuNUMgMjg2LjgwNCw2NjEuNjYxIDE3NC4xMzgsNjYxLjE2MSA2MS41LDY2MEMgMjcuMjAwNyw2NTAuODcyIDYuNTM0MDcsNjI5LjAzOSAtMC41LDU5NC41QyAtMC41LDQ2NC41IC0wLjUsMzM0LjUgLTAuNSwyMDQuNUMgNy4yNzg1MSwxNjcuODg5IDI5LjYxMTgsMTQ1LjcyMiA2Ni41LDEzOEMgMjg4LjUsMTM3LjMzMyA1MTAuNSwxMzcuMzMzIDczMi41LDEzOEMgNzY5LjM2MSwxNDUuNjk1IDc5MS42OTUsMTY3Ljg2MiA3OTkuNSwyMDQuNSBaIE0gMTAzLjUsMTkyLjUgQyAzMDAuNjY2LDE5MS4zMzQgNDk4LDE5MS4xNjcgNjk1LjUsMTkyQyA1OTkuMTY3LDI4OC4zMzMgNTAyLjgzMywzODQuNjY3IDQwNi41LDQ4MUMgNDAxLjgzMyw0ODQuMzMzIDM5Ny4xNjcsNDg0LjMzMyAzOTIuNSw0ODFDIDI5Ni4yOTksMzg0LjYzMiAxOTkuOTY1LDI4OC40NjUgMTAzLjUsMTkyLjUgWiBNIDUyLjUsMjE4LjUgQyAxMTUuNDc5LDI4MC44MTIgMTc4LjE0NiwzNDMuNDc5IDI0MC41LDQwNi41QyAxNzkuMTY3LDQ2OC41IDExNy41LDUzMC4xNjcgNTUuNSw1OTEuNUMgNTQuMTAzOCw1ODkuMDI4IDUzLjI3MDQsNTg2LjM2MiA1Myw1ODMuNUMgNTIuNSw0NjEuODM0IDUyLjMzMzMsMzQwLjE2NyA1Mi41LDIxOC41IFogTSA3NDUuNSwyMTguNSBDIDc0Ni42NjYsMzM5Ljk5OSA3NDYuODMzLDQ2MS42NjYgNzQ2LDU4My41QyA3NDUuNzMsNTg2LjM2MiA3NDQuODk2LDU4OS4wMjggNzQzLjUsNTkxLjVDIDY4MS41LDUzMC4xNjcgNjE5LjgzMyw0NjguNSA1NTguNSw0MDYuNUMgNjIwLjg1NCwzNDMuODEyIDY4My4xODgsMjgxLjE0NiA3NDUuNSwyMTguNSBaIE0gMjc4LjUsNDQ1LjUgQyAyNzkuMjM5LDQ0NS4zNjkgMjc5LjkwNiw0NDUuNTM2IDI4MC41LDQ0NkMgMzA2Ljg2MSw0NzIuNjk1IDMzMy41MjgsNDk5LjAyOCAzNjAuNSw1MjVDIDM4Ni41LDU0MS42NjcgNDEyLjUsNTQxLjY2NyA0MzguNSw1MjVDIDQ2NS42NTIsNDk4LjY4MSA0OTIuNjUyLDQ3Mi4xODEgNTE5LjUsNDQ1LjVDIDU3My42NTMsNDk5LjE1MyA2MjcuNjUzLDU1Mi45ODYgNjgxLjUsNjA3QyA0OTMuNSw2MDcuNjY3IDMwNS41LDYwNy42NjcgMTE3LjUsNjA3QyAxNzEuMzY4LDU1My4yOTkgMjI1LjAzNSw0OTkuNDY1IDI3OC41LDQ0NS41IFoiLz48L2c+CjxnPjxwYXRoIHN0eWxlPSJvcGFjaXR5OjAuOTk3IiBmaWxsPSIjMDU1ZjExIiBkPSJNIDQ4Ny41LDIwNi41IEMgNTAwLjY3NiwyMDYuNTI0IDUwNS4xNzYsMjEyLjg1NyA1MDEsMjI1LjVDIDQ4Ni44NzksMjU2LjQxIDQ3Mi4yMTIsMjg3LjA3NiA0NTcsMzE3LjVDIDQ0NC42MjcsMzQxLjU0NyA0MjguMTI3LDM2Mi4zOCA0MDcuNSwzODBDIDM4NS45MTYsMzk0Ljg2NiAzNjMuNTgyLDM5Ni4yIDM0MC41LDM4NEMgMzE4LjcxNCwzNjUuMzA0IDMxMS44ODEsMzQyLjEzNyAzMjAsMzE0LjVDIDMyNS4wMjcsMzA1LjEzNyAzMzEuODYsMjk3LjMwMyAzNDAuNSwyOTFDIDM1Ny4zODQsMjgzLjM5MSAzNzQuMzg0LDI3Ni4wNTggMzkxLjUsMjY5QyA0MjEuMjMyLDI1NC40NzkgNDQ4LjU2NSwyMzYuNDc5IDQ3My41LDIxNUMgNDc4LjQyOSwyMTIuMzc5IDQ4My4wOTYsMjA5LjU0NiA0ODcuNSwyMDYuNSBaIi8+PC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eToxIiBmaWxsPSIjZmNmY2ZjIiBkPSJNIDQ4OS41LDIxOS41IEMgNDc3LjI4LDI0OC42MzQgNDYzLjc4LDI3Ny42MzQgNDQ5LDMwNi41QyA0MzguNjIsMzI1Ljg3MyA0MjYuMjg2LDM0My44NzMgNDEyLDM2MC41QyA0MDIuMzQ2LDM3MC4yNDQgMzkwLjg0NiwzNzYuNzQ0IDM3Ny41LDM4MEMgMzY0LjY5LDM4Mi4yOTggMzUzLjM1NywzNzkuMjk4IDM0My41LDM3MUMgMzM3LjUwNywzNjMuNTQ3IDMzNy4wMDcsMzU1LjcxMyAzNDIsMzQ3LjVDIDM0Ny4xOTgsMzQxLjk2NyAzNTIuNjk4LDMzNi44IDM1OC41LDMzMkMgNDAxLjc4MywzMDQuNDExIDQ0MS4yODMsMjcxLjkxMSA0NzcsMjM0LjVDIDQ4MS4zNzgsMjI5LjYyNyA0ODUuNTQ1LDIyNC42MjcgNDg5LjUsMjE5LjUgWiIvPjwvZz4KPGc+PHBhdGggc3R5bGU9Im9wYWNpdHk6MSIgZmlsbD0iI2FhYTY5YiIgZD0iTSA0NzAuNSwyMzIuNSBDIDQ3MS4yMzksMjMyLjM2OSA0NzEuOTA2LDIzMi41MzYgNDcyLjUsMjMzQyA0MzguNjU3LDI2OC4yMDEgNDAxLjMyMywyOTguODY4IDM2MC41LDMyNUMgMzUyLjM0OSwzMzEuMTUyIDM0NC44NDksMzM3Ljk4NSAzMzgsMzQ1LjVDIDMzNi4wOTEsMzQ5LjMxNSAzMzQuNzU4LDM1My4zMTUgMzM0LDM1Ny41QyAzMjEuMzY5LDMyNi45MjcgMzMwLjUzNSwzMDUuNzYgMzYxLjUsMjk0QyA0MDEuODg4LDI4MC4xNTQgNDM4LjIyMSwyNTkuNjU0IDQ3MC41LDIzMi41IFoiLz48L2c+Cjwvc3ZnPgo=",
        defaultOutputMappingResultExpressionDescription = "Expression to handle the <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#response\">result</a>. Details in the <a href=\"https://docs.camunda.io/docs/components/connectors/use-connectors/\" target=\"_blank\">documentation</a>.",
        defaultErrorHandlingExpressionDescription = "Expression to handle <a href=\"https://github.com/brix-ag/mail-thymeleaf-connector#errors\">errors</a>. Details in the <a href=\"https://docs.camunda.io/docs/components/connectors/use-connectors/\" target=\"_blank\">documentation</a>."
)
@Getter
@Setter
public class Request {

    @PropertyGroup(
            groupId = "tpt",
            groupName = "Email Template",
            groupTooltip = "The connection to the mail server is configured in the runtime, <a href=\"https://github.com/brix-ag/camunda-connector-utils-spring#configuration-example-yaml\">see here</a>."
    )
    @Valid
    @NotNull
    private TemplateGroup templateGroup;

    @PropertyGroup(
            groupId = "kck",
            groupName = "Keycloak Attributes",
            groupTooltip = "The connection to keycloak is configured in the runtime, <a href=\"https://github.com/brix-ag/camunda-connector-utils-spring#configuration-yaml\">see here</a>."
    )
    @Valid
    private KeycloakGroup keycloakGroup;

    @PropertyGroup(
            groupId = "sjt",
            groupName = "Subject"
    )
    @Valid
    @NotNull
    private SubjectGroup subjectGroup;

    @PropertyGroup(
            groupId = "sdr",
            groupName = "Sender",
            groupTooltip = "Exactly one of the fields has to be filled out."
    )
    @Valid
    @NotNull
    private SenderGroup senderGroup;

    @PropertyGroup(
            groupId = "rcp",
            groupName = "Recipients",
            groupTooltip = "Only one mail is sent to each address e.g. if a user is in several specified groups or roles."
    )
    @Valid
    @NotNull
    private RecipientsGroup recipientsGroup;

    @PropertyGroup(
            groupId = "cps",
            groupName = "Copies"
    )
    @Valid
    @NotNull
    private CopiesGroup copiesGroup;

}
