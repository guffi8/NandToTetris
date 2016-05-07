import java.util.HashMap;

/**
 * Provides a symbol table abstraction. The symbol table associates the identifier names 
 * found in the program with identifier properties needed for compilation: type, kind, 
 * and running index. The symbol table for Jack programs has two nested scopes (class / subroutine).
 */
public class SymbolTable {
	
		private static class VarInfo {
			
			private final String type;
			private final IdentifierKind kind;
			private final int index;
			
			private VarInfo(String type, IdentifierKind kind, int index) {
				this.type = type;
				this.kind = kind;
				this.index = index;
			}
			
			private String getType() {
				return this.type;
			}
			
			private IdentifierKind getKind() {
				return this.kind;
			}
			
			private int getIndex() {
				return this.index;
			}
			
		}
		
		private HashMap<String, VarInfo> classScope;
		private HashMap<String, VarInfo> subroutineScope;
		private boolean isSubroutine;
		
		private int staticIndex;
		private int fieldIndex;
		private int localIndex;
		private int argIndex;
		
		/**
		 * Creates a new empty symbol table.
		 */
		public SymbolTable() {
			this.classScope = new HashMap<>();
			this.subroutineScope = new HashMap<>();
			this.isSubroutine = false;
			this.staticIndex = 0;
			this.fieldIndex = 0;
			this.localIndex = 0;
			this.argIndex = 0;
		}
		
		/**
		 * Starts a new subroutine scope (i.e. resets the subroutine’s symbol table.)
		 */
		public void startSubroutine() {
			this.subroutineScope.clear();
			this.localIndex = 0;
			this.argIndex = 0;
			this.isSubroutine = true;
		}
		
		/**
		 * Returns the number of variables of the given kind already 
		 * defined in the current scope after increasing it in 1.
		 */
		private int getKindIndex(IdentifierKind kind) {
			
			int index;
			
			switch(kind) {
			case ARG:
				index = this.argIndex;
				this.argIndex++;
				break;
			case FIELD:
				index = this.fieldIndex;
				this.fieldIndex++;
				break;
			case STATIC:
				index = this.staticIndex;
				this.staticIndex++;
				break;
			case VAR:
				index = this.localIndex;
				this.localIndex++;
				break;
			default:
				index = -1;
				break;
			
			}
			
			return index;
		}
		
		/**
		 * Defines a new identifier of a given name, type, and kind and assigns it a 
		 * running index. STATIC and FIELD identifiers have a class scope, while ARG 
		 * and VAR identifiers have a subroutine scope.
		 */
		public void define(String name, String type, IdentifierKind kind) {
			VarInfo newElement = new VarInfo(type, kind, getKindIndex(kind));
			if(!isSubroutine) {
				this.classScope.put(name, newElement);
			} else {
				this.subroutineScope.put(name, newElement);
			}
		}
		
		/**
		 * Returns the number of variables of the given kind already defined in 
		 * the current scope.
		 */
		public int varCount(IdentifierKind kind) {
			
			switch(kind) {
			case ARG:
				return this.argIndex;
			case FIELD:
				return this.fieldIndex;
			case STATIC:
				return this.staticIndex;
			case VAR:
				return this.localIndex;
			default:
				return -1;
			}
		}
		
		/**
		 * Returns the kind of the named identifier in the current scope. If 
		 * the identifier is unknown in the current scope, returns NONE .
		 */
		public IdentifierKind kindOf(String name) {

			// if the var name in the subroutine
			if(this.subroutineScope.containsKey(name)) {
				return this.subroutineScope.get(name).getKind();
			}
			if(this.classScope.containsKey(name)){
				return this.classScope.get(name).getKind();
			}
			return null;
		}
		
		/**
		 * Returns the type of the named identifier in the current scope.
		 */
		public String typeOf(String name) {

			// if the var name in the subroutine
			if(this.subroutineScope.containsKey(name)) {
				return this.subroutineScope.get(name).getType();
			}
			if(this.classScope.containsKey(name)){
				return this.classScope.get(name).getType();
			}
			return null;
		}
		
		/**
		 * Returns the index assigned to the named identifier.
		 */
		public int indexOf(String name) {

			// if the var name in the subroutine
			if(this.subroutineScope.containsKey(name)) {
				return this.subroutineScope.get(name).getIndex();
			}
			if(this.classScope.containsKey(name)){
				return this.classScope.get(name).getIndex();
			}
			return -1;
		}
		
}
