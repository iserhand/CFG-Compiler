import java.io.*;
import java.util.*;

public class Main {

	public static String answer = "";
	public static String strinput = "";
	public static String derivation = "";
	public static char limit = '?'; // (Character) null;
	public static Stack<String> DerStack = new Stack<>();
	public static Queue<String> Parsetree = new LinkedList<>();

	public static ArrayList<VariablesAndTerminals> sortedList = new ArrayList<VariablesAndTerminals>();

	static class Node {
		char key;
		Vector<Node> child = new Vector<>();
		Node parent = null;
		boolean dallanma = false;
		boolean visited = false;
		boolean root = false;

	};

	static Node newNode(char key) {
		Node temp = new Node();
		temp.key = key;
		return temp;

	}

	// Prints the n-ary tree level wise
	static void LevelOrderTraversal(Node root) {
		if (root == null)
			return;
		// Standard level order traversal code
		// using queue
		int queuedepth = 0;
		int queuewidth = 0;
		Queue<Node> q = new LinkedList<>(); // Create a queue
		q.add(root); // Enqueue root
		while (!q.isEmpty()) {
			int n = q.size();
			// If this node has children
			while (n > 0) {
				// Dequeue an item from queue
				// and print it
				Node p = q.peek();
				q.remove();
				queuewidth++;
				// System.out.print(p.key + " ");
				// Enqueue all children of
				// the dequeued item
				for (int i = 0; i < p.child.size(); i++)
					q.add(p.child.get(i));
				n--;
			}
			// Print new line between two levels
			queuedepth++;
			// System.out.println();
		}
		char[][] printTree = new char[queuedepth * 4][queuewidth * 4];
		for (int i = 0; i < printTree.length; i++) {
			for (int j = 0; j < printTree[i].length; j++) {
				printTree[i][j] = ' ';
			}
		}
		q = new LinkedList<>();
		q.add(root); // Enqueue root
		int x = 0;
		int y = 0;
		while (!q.isEmpty()) {
			int n = q.size();
			// If this node has children
			while (n > 0) {
				// Dequeue an item from queue
				// and print it
				Node p = q.peek();
				if (p.parent == null) {
					y = queuewidth * 2;
					printTree[x][y] = p.key;
				} else {
					for (int i = 0; i < printTree[0].length; i++) {
						if (printTree[x - 1][i] != ' ' && printTree[x][i] == ' ') {
							printTree[x][i] = p.key;
							y = i;
							break;
						}
					}
				}
				q.remove();
				// Enqueue all children of
				// the dequeued item
				int childCount = p.child.size();
				x++;
				if (childCount > 0) {
					if (childCount % 2 == 0) {
						y = y - (childCount / 2);
						boolean seen = false;
						for (int i = 0; i < childCount + 1; i++) {
							if (printTree[x - 1][y] != p.key) {
								if (seen) {
									printTree[x][y] = '\\';
								} else {
									printTree[x][y] = '/';
								}
								y++;
							} else {
								seen = true;
								y++;
							}
						}
					} else {
						y = y - ((childCount - 1) / 2);
						boolean seen = false;
						for (int i = 0; i < childCount; i++) {
							if (printTree[x - 1][y] != p.key) {
								if (seen) {
									printTree[x][y] = '\\';
								} else {
									printTree[x][y] = '/';
								}
								y++;
							} else {
								seen = true;
								printTree[x][y] = '|';
								y++;
							}
						}
					}
					for (int i = 0; i < p.child.size(); i++) {
						q.add(p.child.get(i));
					}
				}
				n--;
				x--;
			}
			// Print new line between two levels
			x = x + 2;
		}
		for (int i = 0; i < printTree.length; i++) {
			boolean elements = false;
			for (int j = 0; j < printTree[i].length; j++) {
				System.out.print(printTree[i][j]);
				if (printTree[i][j] != ' ') {
					elements = true;
				}
			}
			if (!elements)
				break;
			System.out.println();
		}
	}

	public static void CFG(char nextchar) {
		boolean flagforlimit = true;
		for (VariablesAndTerminals x : sortedList) {
			for (int i = 0; i < x.terminal.length(); i++) {
				if (x.terminal.charAt(i) == nextchar) {
					flagforlimit = false;
					if (x.terminal.length() == 1) {
						limit = '?';
						break;
					} else
						limit = x.terminal.charAt(0);
					break;
				}
			}
			if (flagforlimit == false) {
				break;
			}
		}

		for (VariablesAndTerminals x : sortedList) {
			if (x.terminal.length() <= answer.length()) {
				boolean isOkey = true;
				for (int i = 0; i < x.terminal.length(); i++) {
					if (answer.charAt(answer.length() - x.terminal.length() + i) != x.terminal.charAt(i)) {
						isOkey = false;
						break;
					}
				}
				if (isOkey == true && answer.charAt(answer.length() - 1) != limit) {
					// converting

					answer = answer.substring(0, answer.length() - x.terminal.length());
					answer = answer + x.variable.charAt(0);

					int length = x.terminal.length();
					int inputLength = derivation.length();

					String loc = x.terminal + nextchar;
					int startingIndexofTheStringToReplace;
					if (derivation.indexOf(loc) >= 0) {
						startingIndexofTheStringToReplace = derivation.indexOf(loc);
					} else {
						startingIndexofTheStringToReplace = derivation.indexOf(x.terminal);
					}
					derivation = derivation.substring(0, startingIndexofTheStringToReplace) + x.variable.charAt(0)
							+ derivation.substring(startingIndexofTheStringToReplace + length);
					DerStack.add(derivation);

					CFG(nextchar);

				}

			}

		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Scanner input = new Scanner(System.in);
		ArrayList<VariablesAndTerminals> list = new ArrayList<VariablesAndTerminals>();
		// dosya okumasÄ±
		File file = new File("CFG.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st = "";
		while ((st = br.readLine()) != null) {
			// olasÄ± olasÄ±klarÄ±n liste iÃ§inde tutlmasÄ±
			st = st.replace('|', ':');
			String[] arrOfst = st.split(">");
			String[] arrOftemp = arrOfst[1].split(":");
			for (String str : arrOftemp) {
				VariablesAndTerminals q = new VariablesAndTerminals();
				q.variable = arrOfst[0];
				q.terminal = str;
				list.add(q);
			}
		}
		// listin sortlanmasÄ±
		boolean listFlag = true;
		int max = 0;
		while (listFlag == true) {
			max = 0;
			for (VariablesAndTerminals i : list) {
				if (i.terminal != "empty" && i.terminal.length() > max)
					max = i.terminal.length();
			}
			for (VariablesAndTerminals i : list) {
				if (i.terminal.length() == max) {
					VariablesAndTerminals temp = new VariablesAndTerminals();
					temp.variable = i.variable;
					temp.terminal = i.terminal;
					sortedList.add(temp);
					i.terminal = "empty";
				}
			}
			listFlag = false;
			for (VariablesAndTerminals i : list) {
				if (i.terminal != "empty") {
					listFlag = true;
					break;
				}
			}
		}
		System.out.print("Please enter a string: ");
		strinput = input.next();
		derivation = strinput;
		for (int i = 0; i < strinput.length(); i++) {
			answer = answer + strinput.charAt(i);
			if (i == strinput.length() - 1) {
				CFG('?');
			} else
				CFG(strinput.charAt(i + 1));
		}
		for (int i = 0; i < answer.length(); i++) {
			if (answer.charAt(i) == '#') {
				answer = answer.substring(0, i + 1) + answer.substring(i, answer.length());
				DerStack.add(answer);
			}
		}
		System.out.print("Derivation: ");

		while (!DerStack.isEmpty()) {
			System.out.print(DerStack.peek() + " => ");
			Parsetree.add(DerStack.pop());
		}
		Parsetree.add(strinput);
		System.out.print(strinput);
		System.out.println();
		if (answer.length() > 1) {
			System.out.println("False");
		} else {

			System.out.println("True");
			Node root = newNode(Parsetree.peek().charAt(0));
			root.root = true;
			Node lastnode = root;
			String last = Parsetree.remove();
			boolean firsttime = true;
			System.out.println();
			while (!Parsetree.isEmpty()) {
				String current = Parsetree.remove();
				if (current.length() != last.length()) {
					// Dallanma
					String temp1 = current;
					boolean notseen = true;
					for (int i = last.length() - 1; i >= 0; i--) {
						if (notseen & Character.isUpperCase(last.charAt(i))) {
							notseen = false;
							continue;
						}
						for (int j = 0; j < temp1.length(); j++) {
							if (temp1.charAt(j) == last.charAt(i)) {
								temp1 = temp1.substring(0, j) + temp1.substring(j + 1);
								break;
							}
						}
					}
					for (int i = 0; i < temp1.length(); i++) {
						lastnode.child.add(newNode(temp1.charAt(i)));
						lastnode.child.get(lastnode.child.size() - 1).parent = lastnode;
					}
					lastnode.dallanma = true;
					int index = 1;
					while (!Character.isUpperCase(lastnode.child.get(lastnode.child.size() - index).key)) {
						index++;
					}
					lastnode = lastnode.child.get(lastnode.child.size() - index);
					lastnode.visited = true;

				} else {
					int changedchar = 0;
					for (int i = 0; i < current.length(); i++) {
						if (current.charAt(i) != last.charAt(i))
							changedchar = i;
					}
					if (!firsttime && !Character.isUpperCase(lastnode.key)) {
						boolean flag=true;
						while (!lastnode.dallanma && !lastnode.root) {
							lastnode.visited = true;
							lastnode = lastnode.parent;
							for (int i = lastnode.child.size() - 1; i >= 0; i--) {
								if (i == 0)
									lastnode.dallanma = false;
								if (!lastnode.child.get(i).visited
										&& Character.isUpperCase(lastnode.child.get(i).key)) {
									lastnode = lastnode.child.get(i);
									lastnode.visited = true;
									flag=false;
									break;
								}
							}
							if(!flag)
								break;
						}
					}
					firsttime = false;
					lastnode.child.add(newNode(current.charAt(changedchar)));
					lastnode.child.get(0).parent = lastnode;
					lastnode = lastnode.child.get(0);
				}
				last = current;
			}
			LevelOrderTraversal(root);
		}

	}

}
