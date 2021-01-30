package com.github.jimfan.parser.bowling;


import com.github.jimfan.parser.bowling.BowlingScoreParser.FrameResultContext;
import com.github.jimfan.parser.bowling.BowlingScoreParser.MissFrameWithoutBonusThrowResultContext;
import com.github.jimfan.parser.bowling.BowlingScoreParser.SpareFrameWithBonusThrowResultContext;
import com.github.jimfan.parser.bowling.BowlingScoreParser.StrikeFrameWithBonusThrowResultContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import java.util.ArrayList;
import java.util.List;


public class BowlingScoreListenerImpl extends BowlingScoreBaseListener {

	private List<FrameResult> frameResults = new ArrayList<FrameResult>();
	private Boolean isGameFinished = false;
	private Integer finalGameScore;
	
	public List<FrameResult> getCurrentFrameScores() {
		return new ArrayList<FrameResult>(this.frameResults);
	}
	
	public Boolean isGameFinished() {
		return this.isGameFinished;
	}
	
	public Integer getFinalGameScore() {
		return this.finalGameScore;
	}
	
	@Override
	public void visitErrorNode(ErrorNode node) {
		// TODO Auto-generated method stub
		throw new GameStateException("There appears to be grammatical error in result string");
	}

	@Override
	public void exitFrameResult(FrameResultContext ctx) throws GameStateException {
		
		if (ctx.strikeFrameResult() != null) {
			this.frameResults.add(new FrameResult(FrameResultNature.STRIKE, 10)); 
		}
		else if (ctx.spareFrameResult() != null) {
			// int scoreForThisFrame = Integer.parseInt(ctx.spareFrameResult().getText().substring(0, 1));
			this.frameResults.add(new FrameResult(FrameResultNature.SPARE, 10));
		}
		else if (ctx.missFrameResult() != null) {
			int scoreForThisFrame = Integer.parseInt(ctx.missFrameResult().getText().substring(0, 1));
			this.frameResults.add(new FrameResult(FrameResultNature.MISS, scoreForThisFrame));
		}
		this.tryLookBackOnStrikeAndSpareResult();
	}
	
	@Override
	public void exitStrikeFrameWithBonusThrowResult(StrikeFrameWithBonusThrowResultContext ctx) {
		int scoreForThisFrame = 10;
		int bonusScoreOne = this.decideScoreFromBonusThrow(ctx.getText().substring(1, 2));
		int bonusScoreTwo = this.decideScoreFromBonusThrow(ctx.getText().substring(2));
		this.frameResults.add(new FrameResult(FrameResultNature.SPARE, scoreForThisFrame, bonusScoreOne, bonusScoreTwo));
		this.tryLookBackOnStrikeAndSpareResult();
	}
	
	@Override
	public void exitSpareFrameWithBonusThrowResult(SpareFrameWithBonusThrowResultContext ctx) {
		int scoreForThisFrame = 10;
		int bonusScore = this.decideScoreFromBonusThrow(ctx.getText().substring(2, 3));
		this.frameResults.add(new FrameResult(FrameResultNature.SPARE, scoreForThisFrame, bonusScore, null));
		this.tryLookBackOnStrikeAndSpareResult();
	}
	
	@Override
	public void exitMissFrameWithoutBonusThrowResult(MissFrameWithoutBonusThrowResultContext ctx) {
		int scoreForThisFrame = Integer.parseInt(ctx.T_DIGIT().getText().substring(0, 1));
		this.frameResults.add(new FrameResult(FrameResultNature.MISS, scoreForThisFrame));
		this.tryLookBackOnStrikeAndSpareResult();
	}
	
	@Override
	public void exitLastFrameResult(BowlingScoreParser.LastFrameResultContext ctx) {
		this.isGameFinished = true;
		this.populateFinalScore();
	}
	
	private void tryLookBackOnStrikeAndSpareResult() {
		
		// Look back and settle if (N-1)th frame was spare
		int currentFrameIndex = this.frameResults.size() - 1;
		int prevIndex = currentFrameIndex - 1;
		
		if (prevIndex < 0) return;
		
		FrameResult curResult = this.frameResults.get(currentFrameIndex);
		FrameResult prevResult = this.frameResults.get(prevIndex);
		
		if (currentFrameIndex >= 9 && FrameResultNature.STRIKE.equals(prevResult.getNature())) {
			FrameResult updateLookBackResult = new FrameResult(prevResult.getNature(), prevResult.getScore() + curResult.getScore() + curResult.getBonusScoreOne());
			this.frameResults.set(prevIndex, updateLookBackResult);
		}
		else if (FrameResultNature.STRIKE.equals(prevResult.getNature()) || FrameResultNature.SPARE.equals(prevResult.getNature())) {
			FrameResult updateLookBackResult = new FrameResult(prevResult.getNature(), prevResult.getScore() + curResult.getScore());
			this.frameResults.set(prevIndex, updateLookBackResult);
		}
		
		
		int prevPrevIndex = currentFrameIndex - 2;
		if (prevPrevIndex < 0) return;
		
		FrameResult prevPrevResult = this.frameResults.get(prevPrevIndex);
		
		if (FrameResultNature.STRIKE.equals(prevPrevResult.getNature())) {
			FrameResult updateLookBackResult = new FrameResult(prevPrevResult.getNature(), prevPrevResult.getScore() + curResult.getScore());
			this.frameResults.set(prevPrevIndex, updateLookBackResult);
		}
	}
	
	private void populateFinalScore() {
		int total = 0;
		for (int i = 0; i <= 9; ++i) {
			total += this.frameResults.get(i).getScore();
		}
		FrameResult lastFrame = this.frameResults.get(9);
		total += (lastFrame.getBonusScoreOne() == null ? 0 : lastFrame.getBonusScoreOne());
		total += (lastFrame.getBonusScoreTwo() == null ? 0 : lastFrame.getBonusScoreTwo());
		this.finalGameScore = total;
	}
	
	private Integer decideScoreFromBonusThrow(String text) {
		if ("X".contentEquals(text)) return 10;
		return Integer.parseInt(text);
	}
}
