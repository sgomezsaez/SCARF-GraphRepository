package org.eclipse.Exception;

public class InputWrongType extends Exception{

	private static final long serialVersionUID = 1L;
	private Object any;
	public Object getAny() {
		return any;
	}
	public InputWrongType(Object any){
		
		this.any = any;
		
	}
}
