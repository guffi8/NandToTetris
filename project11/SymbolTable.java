import java.util.HashMap;


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
		
		
		public SymbolTable() {
			this.classScope = new HashMap<>();
			this.subroutineScope = new HashMap<>();
			this.isSubroutine = false;
			this.staticIndex = 0;
			this.fieldIndex = 0;
			this.localIndex = 0;
			this.argIndex = 0;
		}
		
		public void startSubroutine() {
			this.subroutineScope.clear();
			this.localIndex = 0;
			this.argIndex = 0;
			this.isSubroutine = true;
		}
		
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
		
		public void define(String name, String type, IdentifierKind kind) {
			VarInfo newElement = new VarInfo(type, kind, getKindIndex(kind));
			if(!isSubroutine) {
				this.classScope.put(name, newElement);
			} else {
				this.subroutineScope.put(name, newElement);
			}
		}
		
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
