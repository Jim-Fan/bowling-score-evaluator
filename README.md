Parser and evaluator using ANTLR4 and Java, in response to this [coding
dojo question](https://codingdojo.org/kata/Bowling/).

To build with tests followed, run `mvn package` in root directory.

Below is one sample input, in which the newbie player missed all his throws in
first 8 frames, hitting a strike in 9th frame, a spare in 10th frame with a strike
again in the bonus throw:

```
0- 0- 0- 0- 0- 0- 0- 0- X 8/X
```

In each game string, there are exactly 10 tokens. Each token stands for score
of respective frame. While possible patterns of 1st to 9th frame is invariant,
10th frame could happen as a miss, spare with bonus, or strike with bonus.

Lexer tokenises the above string and parser builds syntax tree as it receives
tokens being found. Java class `BowlingScoreListenerImpl` acts as parse tree
traversal listener and also a state machine for evaluating total score as
traversal goes on.

Look at `BowlingScore.g4` for full definition of grammar.

There is no `main` method in this repo. Rather, look at and execute test
cases in `BowlingScoreTest` for expected behaviour of the evaluator.

Reflection: While use of parser generator might seem to be an overkill, it
enforces clean separation of score calculation in various scenario
(hope you would agree with me :smiley: ). Together with use of unit tests,
changes in syntax could be handled rather seamlessly. Last but not least,
ANTLR is a nice tool to learn and work with.
