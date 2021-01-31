package com.github.jimfan.parser.bowling.test;

import com.github.jimfan.parser.bowling.BowlingScoreLexer;
import com.github.jimfan.parser.bowling.BowlingScoreListenerImpl;
import com.github.jimfan.parser.bowling.BowlingScoreParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Ignore;

public class BowlingScoreTest {

	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithStraightZeroResult() {
		int score = evaluateResultForFinalScore("0- 0- 0- 0- 0- 0- 0- 0- 0- 0-");
		Assert.assertEquals(0, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithBasicStrikeFollowedByMiss() {
		int score = evaluateResultForFinalScore("X 2- 3- 0- 0- 0- 0- 0- 0- 0-");
		Assert.assertEquals(20, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithThreeStrikesFollowedByMiss() {
		int score = evaluateResultForFinalScore("X X X 0- 0- 0- 0- 0- 0- 0-");
		Assert.assertEquals(60, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithBasicSpareFollowedByMiss() {
		int score = evaluateResultForFinalScore("5/ 2- 0- 0- 0- 0- 0- 0- 0- 0-");
		Assert.assertEquals(14, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithStraightMissResult() {
		int score = evaluateResultForFinalScore("9- 9- 9- 9- 9- 9- 9- 9- 9- 9-");
		Assert.assertEquals(90, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithFullStrikeResult() {
		int score = evaluateResultForFinalScore("X X X X X X X X X XXX");
		Assert.assertEquals(300, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithFullStrikeSinceNinthFrame() {
		int score = evaluateResultForFinalScore("0- 0- 0- 0- 0- 0- 0- 0- X XXX");
		Assert.assertEquals(60, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_SpareOccurAtTenthFrame() {
		int score = evaluateResultForFinalScore("0- 0- 0- 0- 0- 0- 0- 0- 0- 8/X");
		Assert.assertEquals(20, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_StrikeOccurAtTenthFrame() {
		int score = evaluateResultForFinalScore("0- 0- 0- 0- 0- 0- 0- 0- 0- X12");
		Assert.assertEquals(13, score);
	}
	
	@Test
	public void evaluationShouldGiveCorrectScore_When_InvokedWithFullStrikeAtNinthFrameFollowedBySpare() {
		int score = evaluateResultForFinalScore("0- 0- 0- 0- 0- 0- 0- 0- X 8/X");
		Assert.assertEquals(40, score);
	}
	
	private static int evaluateResultForFinalScore(String gameResult) {
		CharStream stream = CharStreams.fromString(gameResult);
        BowlingScoreLexer lexer = new BowlingScoreLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        BowlingScoreParser parser = new BowlingScoreParser(tokenStream);

        ParseTree tree = parser.root();
        
        ParseTreeWalker walker = new ParseTreeWalker();
        BowlingScoreListenerImpl listener = new BowlingScoreListenerImpl();
    	walker.walk(listener, tree);
    	
    	return listener.getFinalGameScore();
	}
}
