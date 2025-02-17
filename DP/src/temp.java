import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class temp {
	
	public static void DP(int[][] dp, int[][] trace, int[] PSalary, int[] PDemand, int n, int p, int c) {
		int pTemp = p;
		
		for(int i = 1; i <= n;i++) {
			for(int j = 1; j <= dp[0].length;j++) {
				if(p > j) {
					dp[i][j] = 0;
				}
				else {
					if(p < PDemand[i]) {  //yanlış d= 7   p = 5+3 olasa çalışmaz 
						if(p == pTemp) {
							dp[i][PDemand[i]] = dp[i-1][p+1] +  (PDemand[i] - p)*c;
							pTemp = p;
							break;
						}
						else if(pTemp <= PDemand[i]) {
							int min = dp[i-1][p+1] + (PDemand[i-1] - p+1)*c;
							int place = p+1;
							for(int k = p+2; k <= PDemand[i];k++) {
								if(min > (dp[i-1][k] + (PDemand[i-1] - k)*c)) {
									min = dp[i-1][k] + (PDemand[i-1] - k)*c;
									place = k;
								}
							}
							dp[i][PDemand[i]] = dp[i-1][place] +  (PDemand[i] - place)*c;
							pTemp = p;
							break;
						}
						else {  // pTemp > PDemand[i]
							
						}
					}
					
					else if(p > PDemand[i]) {
						if(i != n) {
							if(p == pTemp) {
								for(int k = 1; k <= pTemp - PDemand[i]+1;k++) {
									dp[i][p+k] = dp[i-1][p+1] + PSalary[k-1];  //dp[i][p+1] olması lazım +k-1 yapınca bu kalıyo
								}
								pTemp = p + pTemp - PDemand[i];
								break;
							}
							else {
								int min = dp[i-1][p+1];
								int place = p+1;
								for(int k = p+2; k <= PDemand[i];k++) {
									if(min > dp[i-1][k]) {
										min = dp[i-1][k];
										place = k;
									}
								}
								for(int k = 1; k <= pTemp - PDemand[i]+1;k++) {
									dp[i][p+k] = dp[i-1][place] + PSalary[k-1];  //dp[i][p+1] olması lazım +k-1 yapınca bu kalıyo
								}
								pTemp = p + pTemp - PDemand[i];
								break;
							}	
						}
						else {
							if(p == pTemp) {
								dp[i][p+1] = dp[i-1][PDemand[i-1]];
								break;
							}
							else {
								int min = dp[i-1][p+1];
								int place = p+1;
								for(int k = p+2; k <= PDemand[i];k++) {
									if(min > dp[i-1][k]) {
										min = dp[i-1][k];
										place = k;
									}
								}
								dp[i][p+1] = dp[i-1][place];
								break;
							}
						}	
					}
					
					else if(p == PDemand[i]) {
						if(p == pTemp) {
							dp[i][p+1] = dp[i-1][p+1];
							pTemp = p;
							break;
						}
						else {
							for(int k = 1; k <= pTemp-p+1;k++) {
								dp[i][p+k] = dp[i-1][p+k];  //dp[i][p+1] olması lazım +k-1 yapınca bu kalıyo
							} // pTemp aynı kalmalı o yüzden değiştirmedim
							break;
						}
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
		int[] tempS = new int[] {5,8,10,15,17};
		int[] tempD = new int[] {4,9,2,7,3};
		int[][] dp = new int[1000][1000];
		int[][] trace = new int[1000][1000];
		int n = 5; // planın kaç yıl olacağı
	    int p = 5; //çıkaracağım oyuncu sayısı
	    int c = 10;  //koç fiyatı
		int num = 0;
		
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
		
		num = 0;
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
		
		for(int i = 0; i < dp.length;i++) {
			dp[0][i] = 0;
			dp[i][0] = 0;
		}
		
		DP(dp, trace, tempS, tempD, n, p, c);
		System.out.println(dp[n+1][p+1]);
		
		
	}
}
