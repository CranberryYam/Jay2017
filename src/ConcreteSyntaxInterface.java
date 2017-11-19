
public interface ConcreteSyntaxInterface {
	public Program program();
	public Statement statement();
	public Block statements();
	public Conditional ifStatement();
	public Loop whileStatement();
	public Assignment assignment();
	public Expression expression();
	public Expression conjunction();
	public Expression relation();
	public Expression addition();
	public Expression term();
	public Expression negation();
	public Expression factor();
	public Declarations declarations();
	public void declarationsOfOneline(Declarations ds);
	public Type type();

}
