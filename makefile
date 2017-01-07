JC = javac
JCFLAG = -g
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JCFLAG) $*.java

CLASSES = \
        GameActor.java \
        Brick.java \
        Paddle.java \
        PowerUp.java \
        Ball.java \
        BackGround.java \
        GameUtils.java \
        GameStage.java \
        Breakout.java

MAIN = Breakout

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)
clean:
	$(RM) *.class