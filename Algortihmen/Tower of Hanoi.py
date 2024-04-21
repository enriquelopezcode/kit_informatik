import matplotlib.pyplot as plt
import numpy as np

class Pile:
    def __init__(self, isFull, size, name):
        self.pile = []
        self.size = size
        self.name = name
        # Initially fill the pile if requested
        if(isFull):
            for i in range(0, size):
                self.pile.append(size - i)
                
    def visualize(self):
        box_width = 10
        box_height = self.size

        lines = []
        # Header with the name of the pile centered
        lines.append(f"{self.name:^{box_width + 2}}")
        lines.append("+" + "-" * box_width + "+")
        
        # Create visual representation of the pile
        for i in range(box_height):
            if i < len(self.pile):
                lines.append(f"|{self.pile[i]:^{box_width}}|")
            else:
                lines.append("|" + " " * box_width + "|")
        lines.append("+" + "-" * box_width + "+")
        return lines
        
    def place(self, log):
        # Insert log at the top of the pile
        self.pile.insert(0, log)
        
    def getUpperLog(self):
        # Remove and return the top log
        log = self.pile[0]
        self.pile.pop(0)
        return log
    

class Lair:
    def __init__(self, size):
        self.size = size
        self.forest = Pile(True, size, "Forest")
        self.base = Pile(False, size, "Base")
        self.hideout = Pile(False, size, "Hideout")
        self.num_logs_moved = 0
        
    def visualise(self):
        # Helper function to print the piles side by side
        def print_boxes_side_by_side(*boxes):
            for lines in zip(*boxes):
                print(' '.join(lines))
                
        forest_logs = self.forest.visualize()
        base_logs = self.base.visualize()
        hideout_logs = self.hideout.visualize()
        print_boxes_side_by_side(forest_logs, base_logs, hideout_logs)
    
    def moveLog(self, start, end):
        # Move a log from one pile to another
        moved_log = start.getUpperLog()
        end.place(moved_log)
        self.num_logs_moved += 1
        
    def transferLogs(self, start, goal, temp, n):
        # Recursive function to transfer logs based on the Tower of Hanoi solution
        if n == 1:
            self.moveLog(start, goal)
        else:
            self.transferLogs(start, temp, goal, n-1)
            self.moveLog(start, goal)
            self.transferLogs(temp, goal, start, n-1)
            
    def sort(self):
        # Public method to start sorting process
        self.transferLogs(self.forest, self.hideout, self.base, self.size)
        return self.num_logs_moved

def LogVisualizer(n):
    run_times = []
    
    # Defining the range for the number of logs
    sizes = np.array(range(1, n+1))

    # Calculating the exponential values 2^x
    exponential_values = 2**sizes
    
    for i in range(1, n+1):
        lair = Lair(i)
        run_times.append(lair.sort())
    plt.figure(figsize=(10, 6))
    plt.plot(sizes, run_times, marker='o', label = 'logs moved', color = 'blue')
    plt.plot(sizes, exponential_values, label='2^x', marker='o', color='red')
    plt.title('Number of Logs Moved vs. Log Amount')
    plt.xlabel('Log Amount')
    plt.ylabel('Number of Logs Moved after Sorting')
    plt.grid(True)
    plt.legend()
    plt.show()
