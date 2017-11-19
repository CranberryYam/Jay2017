public interface SyntaxInterface {
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
	public Declarations scanDeclarations(TokensManager tm);
	public void scanDeclarationsOfOneline(Declarations ds,TokensManager tm);
	public Type scanType(Token token);
}
