import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.util.SortedSet;
import java.util.TreeSet;

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
				String.format(model.getName() + "-- %s\n", inst.nb_dividers, " X ", inst.capacity));

				//solver call!
		model.getSolver().solve();
		for(IntVar i : dividers){
			System.out.println(i.getName()+"->"+i.getValue()+";");
		}
		//model.getSolver().printStatistics();

		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		// here!
		
		int  n = inst.nb_dividers-2;
		dividers = new IntVar[n+2];
		dividers[0] = model.intVar("d_"+0, 0, 0, false);
		dividers[n+1] = model.intVar("d_"+(n+2), inst.capacity+1, inst.capacity+1, false);

		for(int i = 1; i < n+1; i++){
			dividers[i] = model.intVar("d_"+i, 2, inst.capacity, false);
		}

		// CONSTRAINTS
		// here!
		model.allDifferent(dividers, "AC").post();
		
		for(int i = 0; i < n+2; i++){
			model.notMember(dividers[i], inst.getExits()).post();
		}
		
	}




	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(dividers)));

	}


}
