package hillbillies.model.statement;

import java.util.*;

import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;

public abstract class Action extends BasicStatement{
		
	public Unit getActor() {
		return this.getTask().getUnit();
	}

	public Expression<?> getTarget() {
		return Expressions.get(0);
	}
	
	public void setTarget(Expression<?> target) {
		if(!Expressions.isEmpty())
			Expressions.remove(0);
		super.addExpression(target);
	}
	
	@Override
	public void reset(){
		this.setCompleted(false);
		this.setInProgress(false);
		
	}
	
	@Override
	public void terminate(){
		this.completed.remove(task);
		this.InProgress.remove(task);
	}
	
	@Override
	public void initialise(Task task){
		this.setTask(task);
		for (Expression<?> exp : Expressions){
			exp.setTask(task);
		}
		if (!this.getCompletedTotal().containsKey(this.getTask()))
			this.setCompleted(false);
		if (!this.InProgress.containsKey(this.getTask()))
			this.InProgress.put(this.getTask(), false);
	}
	
	protected Map<Task, Boolean> InProgress = new HashMap<Task, Boolean>();
	
	public void setInProgress(boolean value){
		this.InProgress.put(this.getTask(), value);
	}
	
	public Boolean getInProgress(){
		return this.InProgress.get(this.getTask());
	}
	
	@Override
	public void execute(){
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
		if (super.getCompleted() || super.getTask().getCounter()<1){
			return;
		}
		super.task.countDown();
		if (!this.getInProgress()){
			this.executeSpecific();
			this.setInProgress(true);
		}
		if (this.getInProgress() && this.getTask().getUnit().isTrulyIdle()){
			this.setCompleted(true);
			this.setInProgress(false);
		}
	}
}
