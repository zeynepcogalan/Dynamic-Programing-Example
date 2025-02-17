import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Zeynep_Çoğalan_2021510078 {
	
	public static void DP(int[][] dp, int[][] trace, int[] PSalary, int[] PDemand, int n, int p, int c) {
		int sum = 0;
		for(int j = 1; j <= n; j++) {  //find sum of demands
			sum = sum + PDemand[j];
		}
		for(int i = 1; i <= n; i++) {  //loop for year
			for(int j = 0; j < sum; j++) {  //loop for players which stay in club and get paid
				if(PDemand[i] == p) {  //If number of demanded players equal to number of player we promote   
					dp[i][j] = dp[i-1][0] + PSalary[j];  //put last year's min cost with 0 player plus salary
					trace[i][j] = 0;
				}
				
				else if(PDemand[i] > p) {  //If number of demanded players is bigger than number of player we promote  
					int min = Integer.MAX_VALUE;
					int place = 0;
					for(int k = 0; k < sum; k++) {  //to find min cost
						if(k+p <= PDemand[i]+j) {  //if all demanded players and all unrented players according to j satisfy, iteration ends
							int num = (PDemand[i] + j - p - k)*c + dp[i-1][k];  //calculate the min cost
							int tempP = k;
							
							if(num < min) {
								min = num;
								place = tempP;
							}
						}
						else break;
					}
					dp[i][j] = min + PSalary[j];
					trace[i][j] = place;
				}
				
				else {  //If number of demanded players is less than number of player we promote
					
					int min = Integer.MAX_VALUE;
					int place = 0;
					if(p - PDemand[i] >= j) {  //If the number of player we can promote with 0 cost is bigger or equal than j 
						dp[i][j] = dp[i-1][0] + PSalary[j];
						trace[i][j] = 0;
					}
					else {  //the number of player we can promote with hiring coaches
						
						for(int k = 0; k < sum; k++) { //to find min cost
							if(j - (p - PDemand[i]) >= k) {  //k can not pass this because after that point we can not promote more player. 
								int num = (j - (p - PDemand[i]) - k)*c + dp[i-1][k];  //calculate the min cost
								int tempP = k;
								
								if(num < min) {
									min = num;
									place = tempP;
								}
							}
							else break;
						}
						dp[i][j] = min + PSalary[j];
						trace[i][j] = place;
					}
				}
			}	
		}
	}

	public static void main(String[] args) {
		File P_Salary = new File("players_salary.txt");
		File P_Demand = new File("yearly_player_demand.txt");
		int[] PSalary = new int[10000];
		int[] PDemand = new int[10000];
		int[][] dp = new int[100][1000];
		int[][] trace = new int[1000][1000];
		int n = 15; // year
	    int p = 5; //promote player
	    int c = 5;  //Coach price 
		int num = 0;
		
		try {
			Scanner in = new Scanner(P_Demand);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if (line != null) {
					if(num != 0) {
						String[] st = line.split("	");
						PDemand[num] = Integer.parseInt(st[1]);  
					}
					num++;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		num = 0;
		try {
			Scanner in = new Scanner(P_Salary);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if (line != null) {
					if(num != 0) {
						String[] st = line.split("	");
						PSalary[num] = Integer.parseInt(st[1]); 
					}
					else 
						PSalary[num] = 0;
					num++;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		for(int i = 0; i <num;i++) {
			dp[0][i] = PSalary[i];
		}
		
		DP(dp, trace, PSalary, PDemand, n, p, c);
	
		int next = 0;
		for(int i = n; i >= 0;i--) {
			if(i == n) {
				System.out.print("|Total cost in "+i+" year "+dp[i][0]+ " |");
				System.out.print(" |Keep "+ 0 + " player in the squad|");
				System.out.print(" |Hire " + ((dp[i][0] - PSalary[0] - dp[i-1][trace[i][0]])  / 10) + " caoches|");
				next = trace[i][0];
			}
			else if(i == 0) {
				System.out.print("|Total cost in "+i+" year "+dp[i][0]+ " |");
				System.out.print(" |Keep "+ next + " player in the squad|");
			}
			else {
				System.out.print("|Total cost in "+i+" year "+dp[i][next]+ " |");
				System.out.print(" |Keep "+ next + " player in the squad|");
				System.out.print(" |Hire " + ((dp[i][next] - PSalary[next] - dp[i-1][trace[i][next]]) / 10) + " caoches|");
				next = trace[i][next];
			}
			System.out.println();
			
		}
	}
}
