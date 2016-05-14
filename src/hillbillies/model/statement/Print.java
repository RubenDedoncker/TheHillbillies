package hillbillies.model.statement;

import hillbillies.model.expression.Expression;

public class Print extends Statement{
	
	public Print(Expression<?> T){
		T.setTask(super.getTask());
		this.setText(T);
	}

	@Override
	public void execute() {
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		System.out.println(this.getText());
		super.setCompleted(true);
	}
	
	public Expression<?> getText() {
		return Text;
	}

	public void setText(Expression<?> text) {
		this.Expressions.add(text);
		Text = text;
	}

	private Expression<?> Text;

}
