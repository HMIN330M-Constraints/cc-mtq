import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

//import static org.chocosolver.Choco.*;

public class CocoAirlines {

	
	IntVar[] dividers;

	Model model;


	public SortedSet<Integer> dividers(int m, int n, int[] exits){
		SortedSet<Integer> sol =  new TreeSet<Integer>();
		return sol;	
	}

	

	public void solve(Instance inst, long timeout, boolean allSolutions) {

		buildModel(inst);
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s x %s", inst.nb_dividers, inst.capacity));

		//System.out.println(st);
		//int i = 0;
		while (model.getSolver().solve()) {	
			//System.out.println("Solution " + ++i);
			/* for(IntVar divider : dividers){
				//System.out.print(divider.getName()+"->"+divider.getValue()+"; ");
			} */

			//System.out.println("");
			if (!allSolutions) break;
		}
		model.getSolver().printStatistics();
		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		int n = inst.nb_dividers-2;
		dividers = new IntVar[n+2];
		dividers[0] = model.intVar("d_"+0, 0, 0, false);
		dividers[n+1] = model.intVar("d_"+(n+1), inst.capacity+1, inst.capacity+1, false);

		for(int i = 1; i < n+1; i++){
			dividers[i] = model.intVar("d_"+i, 3, inst.capacity, false);
		}

		List<IntVar> different = new ArrayList<IntVar>();
		for (int i = 0; i < n + 2; i++) {
			for (int j = i + 1; j < n + 2; j++) {
				different.add(dividers[i].sub(dividers[j]).abs().intVar());
			}
		}

		// CONSTRAINTS
		// here!
		model.allDifferent(dividers, "AC").post();
		
		IntVar[] different_var = new IntVar[different.size()];
		different_var = different.toArray(different_var);
		model.allDifferent(different_var, "AC").post();
		
		for(int i = 0; i < n+2; i++){
			model.notMember(dividers[i], inst.getExits()).post();
		}
		
	}

	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(dividers)));

	}

}
