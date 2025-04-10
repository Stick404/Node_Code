## How the flow will work

First, create a "graph" of nodes that point to each other.
Then, define an end node.
And then, repeat the following:
    - Ask for children, put them into a stack (and save another output stack)
    - If the Child was already in the stack, move it to the top

SEAL/SAVE IT?

Then, repeat the following: (until the saved stack is empty)
    - Check if it needs any data from the Map, if so:
        - Call that data with the UUID into the function
    - Run the first node on the stack, put its output data into a HashMap (UUID, Data)



