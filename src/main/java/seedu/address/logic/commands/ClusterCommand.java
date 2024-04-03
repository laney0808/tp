package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Allergies;
import seedu.address.model.person.BloodType;
import seedu.address.model.person.Condition;
import seedu.address.model.person.Country;
import seedu.address.model.person.DateOfAdmission;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Diagnosis;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.NricContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Sex;
import seedu.address.model.person.Status;
import seedu.address.model.person.StatusContainsKeywordsPredicate;
import seedu.address.model.person.Symptom;
import seedu.address.model.tag.Tag;


/**
 * Updates the details of an existing person in the address book.
 */
public class ClusterCommand extends Command {

    public static final String COMMAND_WORD = "cluster";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the details of the person identified "
            + "by the respective NRIC in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: NRIC "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " S0123456A "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_CLUSTER_FOUND_SUCCESS = "Cluster found! Here are the people in this cluster:";
    public static final String MESSAGE_CLUSTER_NOT_FOUND = "Too few infected people to form a cluster.\n"
            + "But here are infected people in the area to look out for:";
    private final int clusterSize;
    private final Predicate<Person> predicate;

    /**
     * @param clusterSize of the person in the filtered person list to update
     * @param predicate details to update the person with
     */
    public ClusterCommand(int clusterSize, Predicate<Person> predicate) {
        requireNonNull(clusterSize);
        requireNonNull(predicate);

        this.clusterSize = clusterSize;
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        ArrayList<String> statusKeywords = new ArrayList<>();
        statusKeywords.add("UNWELL");
        model.updateFilteredPersonList(new StatusContainsKeywordsPredicate(statusKeywords));

        if (model.getFilteredPersonList().size() >= clusterSize) {
            return new CommandResult(String.format(MESSAGE_CLUSTER_FOUND_SUCCESS));
        } else {
            return new CommandResult(String.format(MESSAGE_CLUSTER_NOT_FOUND));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClusterCommand)) {
            return false;
        }

        ClusterCommand otherClusterCommand = (ClusterCommand) other;
        return otherClusterCommand.clusterSize == clusterSize
                && predicate.equals(otherClusterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("cluster size", clusterSize)
                .add("predicate", predicate)
                .toString();
    }
}
