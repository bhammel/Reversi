JC       = javac
JFLAGS   = -g
CLASSES  = Board.class Player.class Reversi.class ReversiGame.class

.SUFFIXES: .java .class

default: $(CLASSES)

.java.class:
	$(JC) $(JFLAGS) $<

clean:
	rm -f *.class
