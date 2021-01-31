grammar BowlingScore;



/********** Grammar rules **********/
root : singleGameScore EOF;

// In context free grammer, there is no construct such as symbol{9}
// for non-terminal symbol. As a complete game unconditionally consists
// of 10 frames, I opt to explicitly express a game as 9 normal plus
// 1 special frame:
singleGameScore :
    frameResult 
    frameResult 
    frameResult 
    frameResult 
    frameResult 
    frameResult 
    frameResult 
    frameResult 
    frameResult
    lastFrameResult 
    ;

frameResult :
	strikeFrameResult
	|
	spareFrameResult
	|
	missFrameResult
	;

lastFrameResult :
	strikeFrameWithBonusThrowResult
	|
	spareFrameWithBonusThrowResult
	|
	missFrameWithoutBonusThrowResult
	;

strikeFrameResult : T_STRIKE;

spareFrameResult : T_DIGIT T_SPARE;

missFrameResult : T_DIGIT T_MISS;

strikeFrameWithBonusThrowResult :
	T_STRIKE T_STRIKE T_STRIKE
	|
	T_STRIKE T_DIGIT T_DIGIT
	|
	T_STRIKE T_DIGIT T_STRIKE
	|
	T_STRIKE T_STRIKE T_DIGIT
	;

spareFrameWithBonusThrowResult :
	T_DIGIT T_SPARE T_STRIKE
	|
	T_DIGIT T_SPARE T_DIGIT
	;

missFrameWithoutBonusThrowResult : T_DIGIT T_MISS;


/********** Token definition **********/
T_STRIKE : 'X';
T_DIGIT : [0-9];
T_SPARE : '/';
T_MISS : '-';
T_SPACE : [ \t\r\n]+ -> skip;