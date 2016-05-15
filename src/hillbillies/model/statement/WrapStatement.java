package hillbillies.model.statement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hillbillies.model.Task;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.Read;

public abstract class WrapStatement extends Statement{
	
	protected Set<Statement> Substatements = new HashSet<Statement>();
	
	@Override
	public void setTask(Task newtask){
		this.task=newtask;
//		for (Statement sub : Substatements){
//			sub.setTask(newtask);
//		}
//		for (Expression<?> exp : Expressions){
//			exp.setTask(newtask);
//		}
		
	}
	
	@Override 
	public void reset(){
		this.setCompleted(false);
		for (Statement sub : Substatements){
			sub.setCompleted(false);
		} 
	}
	
	public boolean stopLoop(){
		if (this.getWrapStatement()!=null)
			return this.getWrapStatement().stopLoop();
		else {
			System.out.println("Place break inside a loop");
			return false;
		}
	}
	
	@Override
	public boolean isWellFormed(){
		boolean Check = true;
		for (Expression<?> exp : this.Expressions){
			if (exp instanceof Read){
				Check = (exp.getWrapStatement().readVariable(((Read) exp).getName())!=null) && Check;
			}
		}
		for (Statement stat : this.Substatements){
			Check = stat.isWellFormed() && Check ;
		}
		return Check;
	}
	
	@Override
	public void initialise(Task newTask){
		System.out.println("init");
		this.setTask(newTask);
		for (Statement sub : Substatements){
			sub.setWrapStatement(this);
			sub.initialise(newTask);
		} 
		for (Expression<?> exp : Expressions){
			exp.setTask(newTask);
			exp.setWrapStatement(this);
		}
		if (!this.getCompletedTotal().containsKey(this.getTask()))
			this.setCompleted(false);
	}
	

	public void addVariable(String name, Expression<?> value){
		this.getWrapStatement().addVariable(name, value);	
		}
	
	public Expression<?> readVariable(String name){
		if (this.getWrapStatement()!=null)
			return this.getWrapStatement().readVariable(name);
		else {
			System.out.println("Unassigned variable: " + name);
			return null;
		}
	}

}
