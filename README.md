# Mail Thymeleaf Connector

Mail connector using thymeleaf templates and possibility to resolve keycloak users.

The [Mail Server Configuration](https://github.com/brix-ag/camunda-connector-utils-spring#email) and the [Keycloak Connection Configuration](https://github.com/brix-ag/camunda-connector-utils-spring#configuration-yaml) is explained in the [Connector Utils Spring](https://github.com/brix-ag/camunda-connector-utils-spring).

## Property Groups

### Email Template

Contains the email template, fallback locale (this locale will be used unless it is defined by the recipient) and variables to inject.

### Keycloak Attributes

Defines the keycloak attributes available on the user object (optional) like locale and sex. Additional attributes can be specified which are available on the user object injected in the template.

### Subject

The subject consists of a message key and possible replacements. If the message is not found then the message key itself is returned and replacements are still performed if possible.

### Sender

Exactly one of the sender properties has to be filled out. Or to be more precise: exactly one field of this group has to evaluated to a non-empty value.

### Recipients

Comma-separated lists of recipients by email (extended), user id, username, user email, group id, group name, role name. For groups and roles a mail is sent to all users in those groups or having those roles. A mail is only sent once to each unique email address (e.g. if a user is in several specified groups).

## Extended Email Syntax

Example: `John;Doe<john.doe@email.to>en;m` or with white-spaces: `John; Doe <john.doe@email.to> en; m`

Backus-Naur form (BNF):

```text
<extended-email-address> ::= <email-address> | [ <first-name> ";" ] <last-name> "<" <email-address> ">" [ <locale> [ ";" <sex> ] ]
<sex> ::= "m" | "f" | <other>
```

`m` is interpreted as male and `f` as female, anything else is unspecified.

The extended email address must not contain any commas if used with the collection types.

## User Object

The user object for the sender and recipient which is injected into the template if a variable name for it was specified.

### User Object Fields

- String **id**
- String **username**
- String **email**
- String **firstName**
- String **lastName**
- Locale **locale**
- Sex **sex**
- Map&lt;String, String&gt; **attributes**

### Sex Enum Constants

- **UNSPECIFIED**
- **MALE**
- **FEMALE**

## Errors

Everything except problems with recipients (&rarr; [Response](response)) will cause an exception. More details can be found in the error message.

### Error Codes

- **VALIDATION** basic request validation failed
- **INTERNAL** an unexpected internal error
- **SENDER** not exactly one sender or error when retrieving the sender
- **COPIES** illegal email addresses in cc or bcc

## Response

All properties prefixed with "failed" are arrays of strings and return the same values as retrieved by the corresponding recipients input fields (except that the values are split now and not comma-separated).

- **sent** number of mails sent
- **errors** number of errors (if a group or role is not found it only counts as one error)
- **failedEmailAddresses**
- **failedUsersById**
- **failedUsersByUsername**
- **failedUsersByEmail**
- **failedGroupsById**
- **failedGroupsByName**
- **failedRolesByName** 
- **failedUsernamesInGroups**
- **failedUsernamesInRoles**