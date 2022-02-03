//AFTER READING CLASSES:
//PLEASE START READING FROM MAIN, WHICH IS ON ROW 584.
//This way you can keep on how this program goes forward.

package tehtavienjako;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


//Employee-object needs id, what is the max days and max days in row that can be done.
//Also calculators for days that have been done and how many is done in row.
//atWork = saves Integer values 0 or 1. 1 = at work on that day, 0 = not at work.
//skills = what is the knowledge.
//offDays = saves Integer values 0 or 1. 1 = off day, 0 = available.
//workDays = saves Day-objects.
//getId is used when sorting.
class Employee {
    int id;
    int maxWorkDays;
    int maxDaysInRow;
    int daysDone = 0;
    int daysRowNow = 0;
    ArrayList<Integer> atWork = new ArrayList();
    ArrayList<String> skills = new ArrayList();
    ArrayList<Integer> offDays = new ArrayList();
    ArrayList<Day> workDays = new ArrayList();
    
    public Employee(int i, int mW, int mDR){
        this.id = i;
        this.maxWorkDays = mW;
        this.maxDaysInRow = mDR;
    }
    public int getId(){
        return this.id;
    }
}

//Day-object need id.
//skillsNeeded = String value is for what skill/shift, Integer value is for how many employees is needed.
//shiftAndEmployees = tells which employees are doing which specific shift. 
class Day {
    int id;
    TreeMap<String, Integer> skillsNeeded = new TreeMap();
    TreeMap<String, ArrayList<Employee>> shiftAndEmployees = new TreeMap();
    
    public Day(int i){
        this.id = i;
    }
    public int getId(){
        return this.id;
    }
}

public class TehtavienJako {
    
    //sortEmps-method sorts employees by their skill-array's sizes.
    //Employees are ordered by descending order.
    //While the size of already ordered sortedEmps-array isn't as big as the size of employees-array (=all employees),
    //we continue sorting. Sorted array is returned.
    public static ArrayList<Employee> sortEmps(ArrayList<Employee> employees, int allSkills){
        ArrayList<Employee> sortedEmps = new ArrayList();
        int counter = 0;
        
        for(int i=0; i < counter; i++){
            for(Employee e:employees){
                if(e.skills.size() == allSkills-counter){
                    sortedEmps.add(e);
                }
            }
            if(sortedEmps.size() != employees.size()){
                counter++;
            }
        }
        
        return sortedEmps;
    }
    
    
    //selectEmployees-method takes array of all employees, all days, all skills and shifts as parameters.
    //Method starts with going thorugh each day in days-array. From each day we loop through skillsNeeded-map.
    //This means that if we have for example day number 1. We want to check what skill-knowledges are needed on
    //that day and how many of these skills are needed. On day one, we have first on skillsNeeded-map
    //skill A and employees needed is 1.
    public static void selectEmployees(ArrayList<Employee> employees, ArrayList<Day> days, 
            ArrayList<String> allSkills, ArrayList<String> shifts){
        
        HashSet<Employee> exceptions = new HashSet();
        
        for(Day day:days){
            for(Map.Entry<String, Integer> daysSkills:day.skillsNeeded.entrySet()){
                //This counter is for finding as many employees for skill as there is needed.
                int counter = 0;
                
                //employeeForShift-array is needed, because sometimes there are more than one employee needed
                //for shift. Because "not knowing" when there are more than one needed, I thought it's more secure 
                //way to make an array of employees for evert shift.
                ArrayList<Employee> employeeForShift = new ArrayList();
                
                //Employee is searched as many as there is needed in shift.
                //(how many employee is needed = daysSkills.getValue())
                //I started looping employees-array from the end of the array. I know this looks mainiac, because why did
                //I even bothered to sort these employees in descending order by their skills-array size, if I
                //here loop in ascending order... 
                //It actually doesn't matter, because if you check row 158 there I loop from the beginning of
                //employees-array. It worked quite well to loop first from the end and then from beginning. This way
                //I'm first "saving" those employees that have more knowledge and when I really have to use those
                //"more skilful" employees, I start looping from the beginning.
                while(counter < daysSkills.getValue()){
                    boolean employeeFound = false;
                    for(int emp=employees.size()-1; emp >= 0; emp--){
                        //Employee is qualified for shift if:
                        //1. there is knowledge
                        //2. there is less work days done than is the maximum value of work days
                        //3. employee is not already at work on this day
                        //4. employee is available on this day
                        //5. there is less days in row now than is the maximum value of max days in row
                        if(employees.get(emp).skills.contains(daysSkills.getKey()) && employees.get(emp).daysDone < employees.get(emp).maxWorkDays
                                && employees.get(emp).atWork.size() < day.id && employees.get(emp).offDays.get(day.id - 1) == 0 && employees.get(emp).daysRowNow < employees.get(emp).maxDaysInRow){
                            
                            //If employee passed all these demands, we change booelan value employeeFound to true and do
                            //some definitions.
                            //We add employee's atWork-array that she/he is at work on this day, daysDone and daysRowNow + 1,
                            //this Day-object is added to workDays-array and array is sorted by id-values,
                            //employee is added to employeeForShift-array and this array is added to day's shiftAndEmployees-array.
                            employeeFound = true;
                            employees.get(emp).atWork.add(1);
                            employees.get(emp).daysDone++;
                            employees.get(emp).daysRowNow++;
                            employees.get(emp).workDays.add(day);
                            Collections.sort(employees.get(emp).workDays, Comparator.comparingInt(Day::getId));
                            employeeForShift.add(employees.get(emp));
                            day.shiftAndEmployees.put(shifts.get(allSkills.indexOf(daysSkills.getKey())), employeeForShift);
                        
                        }
                        //If employee is found, we break looping through employees-array.
                        if(employeeFound){
                            break;
                        }
                    }
                    
                    //If we can't find employee with first demands we go employees-array through again with new demands.
                    //Now we only care that employee haves the needed skill, is available, and isn't at work already
                    //on this specific day. If daysInRow or daysDone variables are over the limit after this, it is
                    //checked in optimizeShifts-method.
                    if(!employeeFound){
                        for(Employee employee:employees){
                            if(employee.skills.contains(daysSkills.getKey()) && employee.offDays.get(day.id - 1) == 0){
                                if((employee.atWork.size() == day.id && employee.atWork.get(day.id-1).equals(0))
                                        || employee.atWork.size() < day.id){
                                    employeeFound = true;
                                    //If atWork-array already includes this day (size of the list is as big as day id)
                                    //we change the value from 0 to 1.
                                    if(employee.atWork.size() == day.id){
                                        employee.atWork.set(day.id-1, 1);
                                    }
                                    else{
                                        employee.atWork.add(1);
                                    }
                                    employee.daysDone++;
                                    employee.daysRowNow++;
                                    employee.workDays.add(day);
                                    Collections.sort(employee.workDays, Comparator.comparingInt(Day::getId));
                                    employeeForShift.add(employee);
                                    day.shiftAndEmployees.put(shifts.get(allSkills.indexOf(daysSkills.getKey())), employeeForShift);
                                    //Employee is now "exception" and added to exceptions-array.
                                    exceptions.add(employee);
                                }
                            }
                            if(employeeFound){
                                break;
                            }
                        }
                    }
                    counter++;
                }
            }
            //Every employee's atWork-array, that is not at work on this day, is added 0.
            for(Employee employee:employees){
                if(!employee.workDays.contains(day)){
                    employee.daysRowNow = 0;
                    if(employee.atWork.size() == day.id-1){
                        employee.atWork.add(0);
                    }
                    
                }
            }
        }
        
        //If there has occured some exceptions, we optimizeShifts and get newDays with new shiftAndEmployee map values.
        //Before printing shifts we sort employees-array by ids.
        if(!exceptions.isEmpty()){
            ArrayList<Day> newDays = new ArrayList();
            newDays = optimizeShifts(employees, exceptions, days, allSkills, shifts);
            Collections.sort(employees, Comparator.comparingInt(Employee::getId));
            printShifts(newDays, employees);
        }
        //If exceptions haven't occured we only sort employees and print the shifts.
        else{
            Collections.sort(employees, Comparator.comparingInt(Employee::getId));
            printShifts(days, employees);
        }
    }
    
    //Exceptions which have occured in selectEmployees-method are given as exceptionsEmps parameter. Also all employees,
    //days, skills and shifts are given to this method.
    //I wanted to make exceptions-array and add all exceptionEmps to it. I don't know quite why, maybe because I'm the
    //slave of my own habits and I think that arrays are easy to handle...
    //Before starting to optimize, I order employees with orderEmployees-method found on row ..
    public static ArrayList<Day> optimizeShifts(ArrayList<Employee> employees, HashSet<Employee> exceptionEmps, ArrayList<Day> days,
            ArrayList<String> allSkills, ArrayList<String> shifts){
        
        ArrayList<Employee> exceptions = new ArrayList();
        ArrayList<Employee> newExceptions = new ArrayList();
        employees = orderEmployees(employees, days.size());
        exceptions.addAll(exceptionEmps);
        int i = 0;
        int limit = 0;
        
        //I have set the limit of omptimizing rounds to 500. You can change the value here on row 234 and on rows 427, 445.
        next:
        while(limit < 500){
            while(i < exceptions.size()){
                boolean found = false;
                //Looping through employees-array. So that we cant possibly change shifts between two employees
                //we need to check that the employees are not the same and that employee to whom we want to
                //give exceptions-employee's shift is not also included in exceptions-array.
                    for(Employee employee:employees){
                        if(employee.id != exceptions.get(i).id && !exceptions.contains(employee)){
                            
                            //If employee is able to take exceptions-employee's shift, she/he has to have less days done than is
                            //she's/he's maxWorkDays value. Also here we are checking that exception-employee is having
                            //more days done than is accepted.
                            if(employee.daysDone < employee.maxWorkDays && exceptions.get(i).daysDone > exceptions.get(i).maxWorkDays){
                                Day removeDay = null;
                                //We go through exceptions-employee's workDays and after that we go through that day's shiftAndEmployees-map.
                                //From that map we find shift that our exception has.
                                for(Day day:exceptions.get(i).workDays){
                                    String findSkill = null;
                                    for(Map.Entry<String, ArrayList<Employee>> shift:day.shiftAndEmployees.entrySet()){
                                        if(shift.getValue() != null && shift.getValue().contains(exceptions.get(i))){
                                            findSkill = shift.getKey();
                                        }
                                        if(findSkill != null){
                                            break;
                                        }
                                    }
                                    //Here we check that our employee doesn't work on this specific day, is available, our skill that
                                    //we are finding is not null and our employee has the knowledge of this skill.
                                    //After we have found our employee, we do quite same definitions as in selectEmployees-method.
                                    //We add employees daysDone + 1 and decrease exceptions-employee daysDone - 1.
                                    //We also change days shiftAndEmployees content by removing exceptions-employee
                                    //and adding our new employee.
                                    //At the end we also check both exceptions-employee's and employee's daysDone and how many days 
                                    //there's in row now. If rules are broken, this employee is added to newExceptions-array.
                                    if(!employee.workDays.contains(day) && employee.offDays.get(day.id-1) == 0
                                            && findSkill != null && employee.skills.contains(allSkills.get(shifts.indexOf(findSkill)))){
                                        
                                        found = true;
                                        removeDay = day;
                                        employee.atWork.set(day.id-1, 1);
                                        employee.workDays.add(day);
                                        employee.daysDone++;
                                        exceptions.get(i).daysDone--;
                                        ArrayList<Employee> tempEmps = days.get(days.indexOf(day)).shiftAndEmployees.get(findSkill);
                                        if(tempEmps != null){
                                            tempEmps.remove(exceptions.get(i));
                                            tempEmps.add(employee);
                                        }
                                        days.get(days.indexOf(day)).shiftAndEmployees.put(findSkill, tempEmps);
                                        
                                        int row = 0;
                                        for(int inRow=0; inRow<employee.workDays.size(); inRow++){
                                            if(inRow < employee.workDays.size()-2){
                                                if(employee.workDays.get(inRow+1).id - employee.workDays.get(inRow) .id == 1){
                                                    row++;
                                                    if(row > employee.maxDaysInRow){
                                                        break;
                                                    }
                                                }
                                                else{
                                                    row=0;
                                                }
                                            }
                                        }
                                        row = 0;
                                        for(int inRow=0; inRow<exceptions.get(i).workDays.size(); inRow++){
                                            if(inRow < exceptions.get(i).workDays.size()-2){
                                                if(exceptions.get(i).workDays.get(inRow+1).id - exceptions.get(i).workDays.get(inRow) .id == 1){
                                                    row++;
                                                    if(row > exceptions.get(i).maxDaysInRow){
                                                        break;
                                                    }
                                                }
                                                else{
                                                    row=0;
                                                }
                                            }
                                        }
                                        if(employee.daysDone > employee.maxWorkDays || row > employee.maxDaysInRow){
                                            if(!newExceptions.contains(employee)){
                                                newExceptions.add(employee);
                                            }
                                        }
                                        if(exceptions.get(i).daysDone > exceptions.get(i).maxWorkDays || row > exceptions.get(i).maxDaysInRow){
                                            if(!newExceptions.contains(exceptions.get(i))){
                                                newExceptions.add(exceptions.get(i));
                                            }
                                        }
                                    }
                                    if(found){ break; }
                                }
                                //Because I can't remove day from workDays-array when looping it, I here remove
                                //the day from exceptions-employee's workDays-array, which we gave to other employee.
                                if(removeDay != null){
                                    exceptions.get(i).atWork.set(removeDay.id-1, 0);
                                    exceptions.get(i).workDays.remove(removeDay);
                                }
                            }
                            //Else means here that for example exceptions-employee has too many days in row than is accepted or
                            //employee, to whom we are giving the shift, has daysDone-variable as high as maxDays-variable.
                            //Here the other definitions are quite the same.
                            //I wanted to check these two separately, because in my opinion it is more optimal to first check
                            //if there is employees that don't have reached maxDays-value and have the knowledge of
                            //wanted skill.
                            else{
                                Day removeDay = null;
                                for(Day day:exceptions.get(i).workDays){
                                    String findSkill = null;
                                    for(Map.Entry<String, ArrayList<Employee>> shift:day.shiftAndEmployees.entrySet()){
                                        if(shift.getValue() != null && shift.getValue().contains(exceptions.get(i))){
                                            findSkill = shift.getKey();
                                        }
                                        if(findSkill != null){
                                            break;
                                        }
                                    }
                                    if(!employee.workDays.contains(day) && employee.offDays.get(day.id-1) == 0
                                            && findSkill != null && employee.skills.contains(allSkills.get(shifts.indexOf(findSkill)))){
                                        found = true;
                                        removeDay = day;
                                        employee.atWork.set(day.id-1, 1);
                                        employee.workDays.add(day);
                                        employee.daysDone++;
                                        exceptions.get(i).daysDone--;
                                        ArrayList<Employee> tempEmps = days.get(days.indexOf(day)).shiftAndEmployees.get(findSkill);
                                        
                                        if(tempEmps != null){
                                            tempEmps.remove(exceptions.get(i));
                                            tempEmps.add(employee);
                                        }
                                        days.get(days.indexOf(day)).shiftAndEmployees.put(findSkill, tempEmps);
                                        
                                        int row = 0;
                                        for(int inRow=0; inRow<employee.workDays.size(); inRow++){
                                            if(inRow < employee.workDays.size()-2){
                                                if(employee.workDays.get(inRow+1).id - employee.workDays.get(inRow) .id == 1){
                                                    row++;
                                                    if(row > employee.maxDaysInRow){
                                                        break;
                                                    }
                                                }
                                                else{
                                                    row=0;
                                                }
                                            }
                                        }
                                        row = 0;
                                        for(int inRow=0; inRow<exceptions.get(i).workDays.size(); inRow++){
                                            if(inRow < exceptions.get(i).workDays.size()-2){
                                                if(exceptions.get(i).workDays.get(inRow+1).id - exceptions.get(i).workDays.get(inRow) .id == 1){
                                                    row++;
                                                    if(row > exceptions.get(i).maxDaysInRow){
                                                        break;
                                                    }
                                                }
                                                else{
                                                    row=0;
                                                }
                                            }
                                        }
                                        if(employee.daysDone > employee.maxWorkDays || row > employee.maxDaysInRow){
                                            if(!newExceptions.contains(employee)){
                                                newExceptions.add(employee);
                                            }
                                        }
                                        if(exceptions.get(i).daysDone > exceptions.get(i).maxWorkDays || row > exceptions.get(i).maxDaysInRow){
                                            if(!newExceptions.contains(exceptions.get(i))){
                                                newExceptions.add(exceptions.get(i));
                                            }
                                        }
                                        
                                    }
                                    if(found){ break; }
                                }
                                if(removeDay != null){
                                    exceptions.get(i).atWork.set(removeDay.id-1, 0);
                                    exceptions.get(i).workDays.remove(removeDay);
                                }
                            }
                        }
                        if(found){ break; }
                    }
            
                //If we have gone through the whole exceptions-array we check, if there has occured 
                //new exceptions when changing shifts. We add our limit-counter and if we are still under
                //500, we first order our employees, because their workDays-arrays' sizes have changed.
                //After that we remove all from exceptions-array and add all to it from newExceptions-array.
                //Then we continue at "next".
                //If limit is as high as 500, we print all errors and break the loop.
                if(i == exceptions.size() - 1){
                    
                    if(newExceptions.size() > 0){
                        limit++;
                        if(limit < 500){
                            employees = orderEmployees(employees, days.size());
                            exceptions.removeAll(exceptions);
                            exceptions.addAll(newExceptions);
                            newExceptions.removeAll(newExceptions);
                            i = 0;
                            continue next;
                        }
                        else{
                            printErrors(newExceptions, employees);
                            break;
                        }
                    }
                    //If there hasn't occured any new exceptions we set limit to 500, go to "next"
                    //which automatically breaks the loop. I'm not quite sure why I ended up doing this
                    //this way. Maybe there was problems when using only "break;" or it was only when my
                    //code wasn't fully finished. Atleast this works...
                    else{
                        limit = 500;
                        continue next;
                    }
                }
                else{ i++; limit++;}
            }
        }
        //New days are returned.
        return days;
    }
    
    //This method orders employees by the size of their workDays-array's size. Order is ascending.
    //Ordered array is returned.
    public static ArrayList<Employee> orderEmployees(ArrayList<Employee> employees, int allDays){
        ArrayList<Employee> retEmps = new ArrayList();
        for(int i=0; i < allDays; i++){
            for(Employee employee:employees){
                if(employee.workDays.size() == i){
                    retEmps.add(employee);
                }
            }
        }
        return retEmps;
    }
    
    //Before printing, employees are sorted by their ids.
    //Also here I give every employee correct value to daysRowNow-variable, which tells
    //which is the highest days in row value they have.
    public static void printErrors(ArrayList<Employee> exceptions, ArrayList<Employee> employees){
        float errorCounter = 0;
        Collections.sort(exceptions, Comparator.comparingInt(Employee::getId));
        for(Employee employee:employees){
            int row = 0;
            int max = 0;
            for(int inRow=0; inRow < employee.workDays.size(); inRow++){
                if(inRow < employee.workDays.size() - 2){
                    if(employee.workDays.get(inRow + 1).id - employee.workDays.get(inRow).id == 1){
                        row++;
                    }
                    else{
                        if(row > max){
                            max = row;
                        }
                        break;
                    }
                }
                employee.daysRowNow = max;
            }
        }
        
        //Here first are printed employees that have more work days than is accepted and then
        //employees that have days in row over the limit.
        System.out.println("!!!! THESE ERRORS OCCURED !!!!\n");
        System.out.println("DAYS DONE OVER THE MAXIMUM VALUE: ");
        for(Employee employee:exceptions){
            if(employee.daysDone > employee.maxWorkDays){
                errorCounter++;
                System.out.println("Employee ID: " + employee.id);
                System.out.println("Max-value: " + employee.maxWorkDays + ", work days: " + employee.daysDone + "\n");
            }
        }
        System.out.println("\nDAYS DONE OVER THE MAXIMUM VALUE IN ROW: ");
        for(Employee employee:exceptions){
            if(employee.daysRowNow > employee.maxDaysInRow){
                errorCounter++;
                System.out.println("Employee ID: " + employee.id);
                System.out.println("Max-value in row: " + employee.maxWorkDays + ", work days in row: " + employee.daysRowNow + "\n");
            }
        }
        
        //Just for fun, calculation of how many employees have successfully placed.
        float sumOfEmps = employees.size();
        float qualified = sumOfEmps - errorCounter;
        System.out.println("Percentage of how many employees don't break rules: " + Math.round((float)(qualified/sumOfEmps)*100) + "%");
    }
    
    //This method prints the list of shifts for every day.
    public static void printShifts(ArrayList<Day> days, ArrayList<Employee> employees){
        for(Day day:days){
            if(day.id < 10){
                System.out.print("      " + day.id);
            }
            else{
                System.out.print("     " + day.id);
            }
        }
        System.out.println();
        for(Employee employee:employees){
            System.out.print(employee.id);
            for(Day day:days){
                boolean shiftFound = false;
                for(HashMap.Entry<String, ArrayList<Employee>> shifts:day.shiftAndEmployees.entrySet()){
                    if(shifts.getValue() != null && shifts.getValue().contains(employee)){
                        shiftFound = true;
                        if(employee.id < 10){
                            System.out.print("    "+ shifts.getKey() + " ");
                        }
                        else if(employee.id < 100){
                            System.out.print("   "+ shifts.getKey() + "  ");
                        }
                        else{
                            System.out.print("  "+ shifts.getKey() + "   ");
                        }
                    }
                }
                if(!shiftFound){
                    if(employee.id < 10){
                        if(day.id < 10){
                            System.out.print("     - ");
                        }
                        else{
                            System.out.print("    -  ");
                        }
                    }
                    else if(employee.id < 100){
                        if(day.id < 10){
                            System.out.print("    -  ");
                        }
                        else{
                            System.out.print("   -   ");
                        }
                    }
                    else{
                        if(day.id < 10){
                            System.out.print("   -   ");
                        }
                        else{
                            System.out.print("  -    ");
                        }
                    }
                }
            }
            System.out.println();
        }
        
    }
    
    //First all the info is read from Input.txt file. I made an array of it, I thinks it's not the "cleanest"
    //way to do it but anyway.
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedReader input = new BufferedReader ( new InputStreamReader ( new FileInputStream("Input.txt"), "UTF-8" ));
        String line = null;
        ArrayList<String> files = new ArrayList();
        while((line = input.readLine()) != null){
            files.add(line.split(" ")[1]);
        } 
        
        BufferedReader br1 = new BufferedReader ( new InputStreamReader ( new FileInputStream(files.get(0)), "UTF-8" ));
        BufferedReader br2 = new BufferedReader ( new InputStreamReader ( new FileInputStream(files.get(2)), "UTF-8" ));
        BufferedReader br3 = new BufferedReader ( new InputStreamReader ( new FileInputStream(files.get(1)), "UTF-8" ));
        BufferedReader br4 = new BufferedReader ( new InputStreamReader ( new FileInputStream(files.get(3)), "UTF-8" ));
        
        br2.readLine();
        
        ArrayList<String> shifts = new ArrayList();
        ArrayList<Employee> employees = new ArrayList();
        ArrayList<String> allSkills = new ArrayList();
        ArrayList<Employee> sortedEmps = new ArrayList();
        ArrayList<Day> days = new ArrayList();
        
        //BufferReader br1 reads file ShiftCompetenceDemand.txt.
        //In array "shifts" is saved every first token from every row. That means YA, YB.. etc.
        //In String "skill" is saved the needed skill, meaning A, B, C... etc.
        //After these, the line is looped until all tokens are went through.
        //I have made every day an object. Before making a new Day-object, days-array is checked
        //if there already exists Day-object which has id == i.
        //Day object holds map of every needed skill for that day
        //as key-value and how many employees are needed for that skill/shift as value.
        while((line = br1.readLine()) != null){
            StringTokenizer st = new StringTokenizer(line, "\t");
            shifts.add(st.nextToken());
            int tokens = st.countTokens();
            String skill = st.nextToken();
            for(int i=1; i < tokens; i++){
                boolean dayFound = false;
                int skills = Integer.parseInt(st.nextToken());
                for(Day d:days){
                    if(d.id == i){
                        dayFound = true;
                    }
                }
                if(dayFound){
                    for(Day d:days){
                        if(d.id == i){
                            d.skillsNeeded.put(skill, skills);
                        }
                    }
                }
                else{
                    Day day = new Day(i);
                    day.skillsNeeded.put(skill, skills);
                    days.add(day);
                }
            }
        }
        
        //BufferReader br2 reads file PersonalTargets.txt.
        //An Employee-object is made from every employee.
        //Employee-object holds values of maximum work days all and in row.
        while((line = br2.readLine()) != null){
            StringTokenizer st = new StringTokenizer(line, "\t");
            int id = Integer.parseInt(st.nextToken());
            int maxWorkDays = Integer.parseInt(st.nextToken());
            int maxDaysInRow = Integer.parseInt(st.nextToken());
            
            Employee employee = new Employee(id, maxWorkDays, maxDaysInRow);
            employees.add(employee);
        }
        
        //BufferReader br3 reads file EmployeeCompetences.txt.
        //From the first row of the file is taken all possible skill-values in allSkills-array.
        //After that we start checking which skills every Employee has by id.
        //Employee id is saved in variable id and row of skills is saved in empSkills-array.
        //After that we loop through allSkills- and empSkills-array. If empSkills-array includes value "+" on 
        //index i, we find Employee from employees-array that matches id-value and add to it's skills-array
        //this specific skill.
        String skills = br3.readLine();
        StringTokenizer stSkills = new StringTokenizer(skills, " ");
        stSkills.nextToken();
        while(stSkills.hasMoreTokens()){
            String skill = stSkills.nextToken();
            if(!allSkills.contains(skill)){
                allSkills.add(skill);
            }
        }
        while((line = br3.readLine()) != null){
            StringTokenizer st = new StringTokenizer(line, " ");
            int id = Integer.parseInt(st.nextToken());
            ArrayList<String> empSkills = new ArrayList();
            while(st.hasMoreTokens()){
                empSkills.add(st.nextToken());
            }
            for(int i=0; i < allSkills.size(); i++){
                if(empSkills.get(i).equals("+")){
                    for(Employee e:employees){
                        if(e.id == id){
                            e.skills.add(allSkills.get(i));
                        }
                    }
                }
            }
        }
        
        //BufferReader br4 reads file Employees.txt.
        //First we take Employee-id in variable id. 
        //After that, we check if nextToken is either "-" or "V".
        //If token is "-", we add Employee's, which id == id, offDays-array value 0.
        //Else, if value is "V", we add offDays-array value 1.
        //1 = off day, 0 = available.
        while((line = br4.readLine()) != null){
            StringTokenizer st = new StringTokenizer(line, "\t");
            int id = Integer.parseInt(st.nextToken());
            
            while(st.hasMoreTokens()){
                if(st.nextToken().equals("-")){
                    for(Employee e:employees){
                        if(e.id == id){
                            e.offDays.add(0);
                        }
                    }
                }
                else{
                    for(Employee e:employees){
                        if(e.id == id){
                            e.offDays.add(1);
                        }
                    }
                }
            }
        }
        
        //Before selecting employee for each shift, I decided to sort employees with 
        //method sortEmps, which is on row 73.
        sortedEmps = sortEmps(employees, allSkills.size());
        
        //After sorting, we start selecting with method selectEmployees, which is found on row 97.
        selectEmployees(employees, days, allSkills, shifts);
        
    }
    
}