JC       = javac
JFLAGS   = -g
CLASSES  = Player.class Board.class Reversi.class Main.class

.SUFFIXES: .java .class

default: $(CLASSES)

.java.class:
	$(JC) $(JFLAGS) $<

clean:
	rm -f *.class
