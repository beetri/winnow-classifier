package ia2.preprocess.filter;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class StopWordEliminator {
	
	private static String[] stopWordsArray = new String[] {
//															"I", 
														/*	"a",	"about",	"an",	"are", "as", */"at",
															"be",	"by",
															"com",
															"de",
															"en",
//															"for",	"from",
//															"how",
//															"in",	"is",		"it",
//															"la",
//															"on",	"or",
//															"that",	"the",		"this",	"to",
														/*	"was",	"what",		"when",	"where", "who", */"will", "with",
//															"und",
//															"www"
															};
	private static String[] allStopWordsArray = new String[] {
															"I", 
															"a",	"about",	"an",	"are", "as", "at",
															"be",	"by",
															"com",
															"de",
															"en",
															"for",	"from",
															"how",
															"in",	"is",		"it",
															"la",
															"on",	"or",
															"that",	"the",		"this",	"to",
															"was",	"what",		"when",	"where", "who", "will", "with",
															"und",
															"www"
															};
	
	@SuppressWarnings("unused")
	private static Set<String> stopWords = new HashSet<String>(){
		{
//		     add("I");
//		     add("a");
//		     add("about");
//		     add("an");
//		     add("are");
//		     add("as");
		     add("at");
//		     add("be");
//		     add("by");
		     add("com");
		     add("de");
		     add("en");
//		     add("for");
//		     add("from");
//		     add("how");
//		     add("in");
//		     add("is");
//		     add("it");
//		     add("la");
//		     add("of");
//		     add("on");
//		     add("or");
//		     add("that");
//		     add("the");
//		     add("this");
//		     add("to");
//		     add("was");
//		     add("what");
//		     add("when");
//		     add("where");
//		     add("who");
//		     add("will");
		     add("with");
//		     add("und");
//		     add("the");
//		     add("www");		     
		}
	};
	
	public static boolean isStopWord(String word){
		return stopWords2.contains(word);
	}
	
	private static Set<String> stopWords2 = new HashSet<String>(){
		{
			add("a");
			add("about");
			add("above");
			add("across");
			add("after");
			add("afterwards");
			add("again");
			add("against");
			add("all");
			add("almost");
			add("alone");
			add("along");
			add("already");
			add("also");
			add("although");
			add("always");
			add("am");
			add("among");
			add("amongst");
			add("amoungst");
			add("amount");
			add("an");
			add("and");
			add("another");
			add("any");
			add("anyhow");
			add("anyone");
			add("anything");
			add("anyway");
			add("anywhere");
			add("are");
			add("around");
			add("as");
			add("at");
			add("back");
			add("be");
			add("became");
			add("because");
			add("become");
			add("becomes");
			add("becoming");
			add("been");
			add("before");
			add("beforehand");
			add("behind");
			add("being");
			add("below");
			add("beside");
			add("besides");
			add("between");
			add("beyond");
			add("bill");
			add("both");
			add("bottom");
			add("but");
			add("by");
			add("call");
			add("can");
			add("cannot");
			add("cant");
			add("co");
			add("computer");
			add("con");
			add("could");
			add("couldnt");
			add("cry");
			add("de");
			add("describe");
			add("detail");
			add("do");
			add("done");
			add("down");
			add("due");
			add("during");
			add("each");
			add("eg");
			add("eight");
			add("either");
			add("eleven");
			add("else");
			add("elsewhere");
			add("empty");
			add("enough");
			add("etc");
			add("even");
			add("ever");
			add("every");
			add("everyone");
			add("everything");
			add("everywhere");
			add("except");
			add("few");
			add("fifteen");
			add("fify");
			add("fill");
			add("find");
			add("fire");
			add("first");
			add("five");
			add("for");
			add("former");
			add("formerly");
			add("forty");
			add("found");
			add("four");
			add("from");
			add("front");
			add("full");
			add("further");
			add("get");
			add("give");
			add("go");
			add("had");
			add("has");
			add("hasnt");
			add("have");
			add("he");
			add("hence");
			add("her");
			add("here");
			add("hereafter");
			add("hereby");
			add("herein");
			add("hereupon");
			add("hers");
			add("herself");
			add("him");
			add("himself");
			add("his");
			add("how");
			add("however");
			add("hundred");
			add("i");
			add("ie");
			add("if");
			add("in");
			add("inc");
			add("indeed");
			add("interest");
			add("into");
			add("is");
			add("it");
			add("its");
			add("itself");
			add("keep");
			add("last");
			add("latter");
			add("latterly");
			add("least");
			add("less");
			add("ltd");
			add("made");
			add("many");
			add("may");
			add("me");
			add("meanwhile");
			add("might");
			add("mill");
			add("mine");
			add("more");
			add("moreover");
			add("most");
			add("mostly");
			add("move");
			add("much");
			add("must");
			add("my");
			add("myself");
			add("name");
			add("namely");
			add("neither");
			add("never");
			add("nevertheless");
			add("next");
			add("nine");
			add("no");
			add("nobody");
			add("none");
			add("noone");
			add("nor");
			add("not");
			add("nothing");
			add("now");
			add("nowhere");
			add("of");
			add("off");
			add("often");
			add("on");
			add("once");
			add("one");
			add("only");
			add("onto");
			add("or");
			add("other");
			add("others");
			add("otherwise");
			add("our");
			add("ours");
			add("ourselves");
			add("out");
			add("over");
			add("own");
			add("part");
			add("per");
			add("perhaps");
			add("please");
			add("put");
			add("rather");
			add("re");
			add("same");
			add("see");
			add("seem");
			add("seemed");
			add("seeming");
			add("seems");
			add("serious");
			add("several");
			add("she");
			add("should");
			add("show");
			add("side");
			add("since");
			add("sincere");
			add("six");
			add("sixty");
			add("so");
			add("some");
			add("somehow");
			add("someone");
			add("something");
			add("sometime");
			add("sometimes");
			add("somewhere");
			add("still");
			add("such");
			add("system");
			add("take");
			add("ten");
			add("than");
			add("that");
			add("the");
			add("their");
			add("them");
			add("themselves");
			add("then");
			add("thence");
			add("there");
			add("thereafter");
			add("thereby");
			add("therefore");
			add("therein");
			add("thereupon");
			add("these");
			add("they");
			add("thick");
			add("thin");
			add("third");
			add("this");
			add("those");
			add("though");
			add("three");
			add("through");
			add("throughout");
			add("thru");
			add("thus");
			add("to");
			add("together");
			add("too");
			add("top");
			add("toward");
			add("towards");
			add("twelve");
			add("twenty");
			add("two");
			add("un");
			add("under");
			add("until");
			add("up");
			add("upon");
			add("us");
			add("very");
			add("via");
			add("was");
			add("we");
			add("well");
			add("were");
			add("what");
			add("whatever");
			add("when");
			add("whence");
			add("whenever");
			add("where");
			add("whereafter");
			add("whereas");
			add("whereby");
			add("wherein");
			add("whereupon");
			add("wherever");
			add("whether");
			add("which");
			add("while");
			add("whither");
			add("who");
			add("whoever");
			add("whole");
			add("whom");
			add("whose");
			add("why");
			add("will");
			add("with");
			add("within");
			add("without");
			add("would");
			add("yet");
			add("you");
			add("your");
			add("yours");
			add("yourself");
			add("yourselves");
		}
	};
	

}
