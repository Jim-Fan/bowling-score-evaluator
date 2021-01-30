Parser and evaluator using ANTLR4 and Java, in response to [this coding
dojo question](https://codingdojo.org/kata/Bowling/).

Look at BowlingScore.g4 for definition of the bowling game result grammar.

Java class `BowlingScoreListenerImpl` acts as parse tree traversal
listener and also a state machine for evaluating total score as traversal
happens.

There is no main method in this repo. Rather, look at and execute test
cases in `BowlingScoreTest` for expected behaviour of the evaluator.
