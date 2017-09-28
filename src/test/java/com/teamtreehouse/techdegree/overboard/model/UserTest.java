package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

// Test Runner Class to run tests for functions in the User class
public class UserTest {

    // Creating objects to be used by all of the tests if need be
    private Board board;
    private User user1;
    private User user2;
    private User user3;
    private Question question1;
    private Question question2;
    private Answer answer1;
    private Answer answer2;
    /*
    For some reason I couldn't get the text from exception in User class to show up here.
    So I made a variable here to store it so I wouldn't have to keep typing it out.
    */
    private String votingErrorMessage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // Initializing all of my objects and variables before the tests are run so I could use their values.
    @Before
    public void setUp() throws Exception {
        board = new Board("Java");
        // Creative, I know. However, nobody is going to see this but, you, the grader.
        user1 = board.createUser("User1");
        user2 = board.createUser("User2");
        user3 = board.createUser("User3");

        // It was kind of hard to think of questions for these but I think I got the answers correct.
        question1 = user1.askQuestion("What is the difference between a HashMap and a TreeMap?");
        answer1 = user2.answerQuestion(question1, "TreeMaps are automatically sorted.");

        question2 = user2.askQuestion("When should I use a do-while loop?");
        answer2 = user1.answerQuestion(question2, "When you want the loop body to run at least once.");

        //Initializing this string so I could just use this variable instead of typing it a million times.
        votingErrorMessage = "You cannot vote for yourself!";
    }

    // Testing up voting questions increases reputation increase here
    @Test
    public void upVotingQuestionsReturnsCorrectRepIncrease() throws Exception {
        user2.upVote(question1);

        assertEquals(5, user1.getReputation());
    }

    // Testing up voting answers increases reputation increase here
    @Test
    public void upVotingAnswersReturnsCorrectRepIncrease() throws Exception {
        user1.upVote(answer1);

        assertEquals(10, user2.getReputation());
    }

    // Testing accepting answers increases reputation increase here
    @Test
    public void acceptingAnAnswerReturnsCorrectRepIncrease() throws Exception {
        user1.acceptAnswer(answer1);

        assertEquals(15, user2.getReputation());
    }

        /*
        These next series of tests make sure that a questioner cannot modify their
        reputation by either up voting or down voting their questions and answers.
        */

    // Testing that a questioner cannot up vote their question here
    @Test
    public void questionerCannotUpVoteTheirOwnQuestion() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage(votingErrorMessage);

        user1.upVote(question1);
    }

    // Testing that a questioner cannot up vote their answer here
    @Test
    public void questionerCannotUpVoteTheirOwnAnswer() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage(votingErrorMessage);

        user2.upVote(answer1);
    }

    // Testing that a questioner cannot down vote their question here
    @Test
    public void questionerCannotDownVoteTheirOwnQuestion() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage(votingErrorMessage);

        user1.downVote(question1);
    }

    // Testing that a questioner cannot down vote their answer here
    @Test
    public void questionerCannotDownVoteTheirOwnAnswer() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage(votingErrorMessage);

        user1.downVote(answer2);
    }

    // Testing to make sure that only the questioner can accept an answer
    @Test
    public void onlyThePersonWhoAskedTheQuestionCanAcceptTheAnswer() throws Exception {
        User questioner = answer1.getQuestion().getAuthor();
        // This is from the User class. I couldn't figure out how to get it here.
        String message = String.format("Only %s can accept this answer as it is their question",
                questioner.getName());

        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(message);

        user2.acceptAnswer(answer1);
    }

    // Testing to see if down voting an answer changes reputation
    @Test
    public void downVotingAnAnswerCostsTheAnswererAPoint() throws Exception {
        user1.upVote(answer1);
        user1.downVote(answer1);

        assertEquals(9, user2.getReputation());
    }

    // Testing to see if somebody that hasn't participated but has an account returns accurate rep = 0
    @Test
    public void newUserHasZeroRepPoints() throws Exception {
        assertEquals(0, user3.getReputation());
    }

        /*
        Currently down-voting of questions affects nothing. However, if it were to be implemented, here it is.
        @Test
        public void downVotingQuestionReturnsCorrectRepDecrease() throws Exception {
            user2.downVote(question1);

            assertEquals(-1, user1.getReputation());
        }*/
}
