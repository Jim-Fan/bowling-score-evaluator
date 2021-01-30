package com.github.jimfan.parser.bowling;

public class FrameResult {

	private FrameResultNature nature;
	private Integer score;
	private Integer bonusScoreOne;
	private Integer bonusScoreTwo;
	
	public FrameResult(FrameResultNature nature, Integer score, Integer bonusOne, Integer bonusTwo) {
		this.nature = nature;
		this.score = score;
		this.bonusScoreOne = bonusOne;
		this.bonusScoreTwo = bonusTwo;
	}
	
	public FrameResult(FrameResultNature nature, Integer score) {
		this(nature, score, null, null);
	}

	public FrameResultNature getNature() {
		return nature;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public Integer getBonusScoreOne() {
		return bonusScoreOne;
	}

	public Integer getBonusScoreTwo() {
		return bonusScoreTwo;
	}

	@Override
	public String toString() {
		return String.format("[%s,%s]", this.score.toString(), this.nature.toString());
	}
}
