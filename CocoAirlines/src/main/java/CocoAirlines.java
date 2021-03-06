import static org.chocosolver.solver.search.strategy.Search.*;
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
	IntVar[] different_var;

	Model model;


	public SortedSet<Integer> dividers(int m, int n, int[] exits){
		SortedSet<Integer> sol =  new TreeSet<Integer>();
		return sol;	
	}

	

	public void solve(Instance inst, long timeout, boolean allSolutions) {

		buildModel(inst);
		this.configureSearch();
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s x %s", inst.nb_dividers, inst.capacity));

		// System.out.println(st);
		int i = 0;
		while (model.getSolver().solve()) {	
			// System.out.println("Solution " + ++i);
			/* for(IntVar divider : dividers){
				// System.out.print(divider.getName()+"->"+divider.getValue()+"; ");
			} */

			// System.out.println("");
			if (!allSolutions) break;
		}
		model.getSolver().printStatistics();
		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		// n est le nombre  de diviseur mobile
		int n = inst.nb_dividers-2; 
		dividers = new IntVar[n+2];

		// les diviseurs fixes
		dividers[0] = model.intVar("d_"+0, 0, 0, false);
		dividers[n+1] = model.intVar("d_"+(n+1), inst.capacity, inst.capacity, false);

		// les diviseurs mobiles
		for(int i = 1; i < n+1; i++){
			dividers[i] = model.intVar("d_"+i, 2, inst.capacity-1, false);
		}

		// les differences entre chaque diviseurs (mobiles et fixes)
		List<IntVar> different = new ArrayList<IntVar>();
		for (int i = 0; i < n + 2; i++) {
			for (int j = i + 1; j < n + 2; j++) {
				different.add(dividers[i].sub(dividers[j]).abs().intVar());
			}
		}

		// CONSTRAINTS
		// exits
		for(int i = 0; i < n+2; i++){
			model.notMember(dividers[i], inst.getExits()).post();
		}

		// pour ??viter les doublons dans les r??sultats
		for(int i=0; i < n+1; i++) {
			model.arithm(dividers[i], "<", dividers[i+1]).post();
		}

		different_var = new IntVar[different.size()];
		different_var = different.toArray(different_var);
		model.allDifferent(different_var, "AC").post();
	}

	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(dividers)));
		//model.getSolver().setSearch(domOverWDegSearch(append(dividers)));
	}

}
