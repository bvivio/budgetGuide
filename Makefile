default:
	@javac bg/*.java

clean:
	@rm bg/*.class

run:
	@java bg.Main
