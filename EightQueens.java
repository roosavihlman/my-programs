package eightqueens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EightQueens {
    //Counter counts all founded solutions.
    public static int counter = 0;
    
    //findSolutions-method first makes ArrayList firstPlaces, which holds all places on the
    //first row. (e.g. 1,1; 1,2; 1,3; 1,4)
    public static void findSolutions(ArrayList<int[]> allPlaces, int queens){
        ArrayList<int[]> firstPlaces = new ArrayList();
        
        for(int[] firstRow:allPlaces){
            if(firstRow[0] == 1){
                firstPlaces.add(firstRow);
            }
        }
        //Main idea in this method is to loop through the firstPlaces list.
        //1. We make pairs-list and add value from firstPlace-list to it.
        //2. We give recMethod-method allPlaces-list, pairs-list and number of queens as parameters.
        for(int i=0; i < firstPlaces.size(); i++){
            ArrayList<int[]> pairs = new ArrayList();
            pairs.add(firstPlaces.get(i));
            recMethod(allPlaces, pairs, queens);
            
        }
        //If solutions have or have not found, program will inform.
        if(counter != 0){
            System.out.println("All solutions: " + counter);
        }
        else{
            System.out.println("Solutions not found.");
        }
    }
    
    //recMethod calls itself. Takes in allPlaces-list, pairs-list and number of queens as parameters.
    //allPlaces indicates all places on "board".
    //pairs indicates all founded places asfar.
    public static void recMethod(ArrayList<int[]> allPlaces, ArrayList<int[]> pairs, int queens){
        //We make placesFound-list. Values will be added to this list if they are possible
        //places for queens. Before adding any places, every place is compared to already
        //possible places on pairs-list.
        ArrayList<int[]> placesFound = new ArrayList();
        
        //thisRow-variable indicates which rows places we are examining. Value is set
        //by getting last value and it's row number from pairs-list. Adding +1 to that, we'll get
        //the number of which row we are checking.
        int thisRow = pairs.get(pairs.size()-1)[0] + 1;
        
        //Before going to check any possible places, we check if pairs-list's size is already
        //as big as the number of queens. If it is, we'll go check the diagonal fit.
        if(pairs.size() == queens){
            checkDiagonal(pairs, queens);
        }
        //Here we loop through allPlaces.
        //1. If rowPlace's row from allPlaces-list matches earlier defined thisRow-variable, we'll continue,
        //  we are on right row.
        //2. Then we'll loop through pairs-list (as checkPlace), which included all already founded, possible places.
        //3. If the vertical distance of rowPlace and checkPlace is 1, we'll then check the horizontal distance.
        //      3.1. If the horizontal distance is 1 or -1 or 0, it means that this exact place isn't possible
        //          for next queen. We'll set distance=true (if there is any checkPlace in the distance of 1/-1/0, this place isn't allowed).
        //4. Else if vertical distance is bigger than 1 (hox: vertical distance of 0 is not possible here, 
        //  beacuse previous defining of thisRow), we'll see if both places are in the same column.
        //      4.1 If both places have the same column, distance is set to true, for same reasons as described in 3.1.
        //5. In the end we confirm that rowPlace's, which we were examining, row was same as thisRow-variable and distance is false.
        //      5.1 This place will be added to placesFound-list.
        else{
            for(int[] rowPlace:allPlaces){
                boolean distance = false;
                if(rowPlace[0] == thisRow){
                    for(int[] checkPlace:pairs){
                        if(rowPlace[0] - checkPlace[0] == 1){
                            if(rowPlace[1] - checkPlace[1] == 1 || rowPlace[1] - checkPlace[1] == -1
                                || rowPlace[1] - checkPlace[1] == 0){
                                distance = true;
                            }
                        }
                        else{
                            if(rowPlace[1] - checkPlace[1] == 0){
                                distance = true;
                            }
                        }
                    }
                }
                if(rowPlace[0] == thisRow && distance == false){
                    placesFound.add(rowPlace);
                }
            }
        }
        
        //All the possible places we have found on this row, we'll loop through.
        //1. On every loop, we add one placesFound-place to pairs-list. 
        //2. We give this exact same recMethod allPlaces, pairs-list with one new plaace and number of queens.
        //  -> Doing this recursive method we can check every possible place's capability to lead
        //      in final solution. If this loop doesn't find places from next row, it goes back to last rows 
        //      this exact loop and takes next place from placesFound-list. Because this method doesn't return
        //      anything, it just goes as far as it can. If this method doesn't find 
        //      one place from every row, it never calls checkDiagonal-method to find the final solutions.
        //3. Before next loop, we delete last value (=this round's placeFound-place) from pairs-list.
        for(int i=0; i < placesFound.size(); i++){
            pairs.add(placesFound.get(i));
            recMethod(allPlaces, pairs, queens);
             
            pairs.remove(pairs.size()-1);
        }
    }
    
    //This method check's, if the founded solution is diagonally acceptable.
    //This takes possible solution as list and number of queens as parameters.
    public static void checkDiagonal(ArrayList<int[]> list, int queens){
        boolean diagonal = false;
        
        //1. We go through the list we got as parameter and first check that it isn't on the last row.
        //  (we go this examination from up to down)
        //2. We go through the same list but checking that value a has lower row number than value b.
        //3. If value a is on column 1, we only go to down rigth.
        //4. If value a is on column queens, we only go to down left.
        //5. In other cases, we check both down right and left.
        //6. If in any cases there are queens who threat eachother, diagonal will get value true.
        //7. If diagonal is false at the end of the examination, we call printSolutions-method.
        for(int[] a:list){
            if(a[0] != queens){
                for(int[] b:list){
                    if(b[0] > a[0]){
                        if(a[1] == 1){
                            for(int i=1; i <= queens - a[0]; i++){
                                if(b[0] == a[0] + i && b[1] == a[1] + i){
                                    diagonal = true;
                                }
                            }
                    
                        }
                        else if(a[1] == queens){
                            for(int i=1; i <= queens - a[0]; i++){
                                if(b[0] == a[0] + i && b[1] == a[1] - i){
                                    diagonal = true;
                                }
                            }
                        }
                        else{
                            for(int i=1; i <= queens - a[0]; i++){
                                if(b[0] == a[0] + i && b[1] == a[1] + i){
                                    diagonal = true;
                                }
                            }
                            if(diagonal == false){
                                for(int i=1; i < a[1]; i++){
                                if(b[0] == a[0] + i && b[1] == a[1] - i){
                                    diagonal = true;
                                }
                            }
                            }
                        }
                    }
                }
            }
        }
        if(diagonal == false){
            printSolutions(list, queens);
        }
    }
    
    //This method gets solution-list (solution, which has all distances ok) and number of queens as parameters.
    public static void printSolutions(ArrayList<int[]> solution, int queens){
        //This is only styling, before printing the first line, we add one to counter. This indicates
        //the number of every solution before printing the "board".
        //Printing the "board" takes couple of loops to print it quite nice.
        System.out.println("Solution number: " + ++counter);
        for(int row=1; row <= queens; row++){
                for(int col=1; col <= queens; col++){
                    for(int[] a:solution){
                        if(a[0] == row){
                            if(a[1] == col){
                                System.out.print("Q  ");
                            }
                            else{
                                System.out.print("-  ");
                            }
                        }
                    }
                }
                System.out.print("\n");
            }
        System.out.println("\n");
    }
    
    //First we ask user how many queens. After that we insert the value in variable queens.
    public static void main(String[] args) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("How many Queens: ");
        int queens = Integer.parseInt(buffer.readLine());
        System.out.println("\n");
        
        //We'll make ArrayList places, where we'll insert all the places on "board".
        ArrayList<int[]> places = new ArrayList(); 
        for(int row=1; row <= queens; row++){
            for(int col=1; col <= queens; col++){
                int[] placeNums = new int[2];
                placeNums[0] = row;
                placeNums[1] = col;
                places.add(placeNums);
            }
        }
        
        //findSolutions-method will get places-list and the number of queens as parameter.
        findSolutions(places, queens);
    }
}


