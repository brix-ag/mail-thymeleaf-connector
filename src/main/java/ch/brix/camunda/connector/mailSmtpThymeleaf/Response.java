package ch.brix.camunda.connector.mailSmtpThymeleaf;

import ch.brix.camunda.connector.util.gson.delimitedCollections.sets.CommaSeparatedStringSet;
import ch.brix.camunda.connector.util.gson.user.*;
import lombok.Data;

@Data
public class Response {

    private long sent;
    /**
     * Increased for every illegal mail address and things that cannot be found, not finding a group only increases the count by 1.
     *
     * Only errors regarding the recipients are collected in the response, other errors are more severe and lead to exceptions.
     */
    private long errors;

    private ExtendedEmailAddressSet failedEmailAddresses = new ExtendedEmailAddressSet();
    private UserByIdSet failedUsersById = new UserByIdSet();
    private UserByUsernameSet failedUsersByUsername = new UserByUsernameSet();
    private UserByEmailSet failedUsersByEmail = new UserByEmailSet();
    private GroupByIdSet failedGroupsById = new GroupByIdSet();
    private GroupByNameSet failedGroupsByName = new GroupByNameSet();
    private RoleByNameSet failedRolesByName = new RoleByNameSet();

    private CommaSeparatedStringSet failedUsernamesInGroups = new CommaSeparatedStringSet();
    private CommaSeparatedStringSet failedUsernamesInRoles = new CommaSeparatedStringSet();

    void incrementSent() {
        sent++;
    }

    void incrementErrors() {
        errors++;
    }

}
