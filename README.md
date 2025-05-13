Knight Board Challenge
This project simulates a knight moving on a board based on a series of directional commands, while avoiding obstacles and handling edge boundaries.

🚀 How It Works
The app fetches a board.json and a commands.json from remote URLs (or environment variables).

It executes movement instructions sequentially:

START x,y,DIRECTION — Sets initial position and facing.

ROTATE DIRECTION — Rotates to a new cardinal direction.

MOVE N — Moves forward N steps unless blocked or out of bounds.

🔧 Setup & Run
Make sure you have Java 17+.

bash
Copy
Edit
export BOARD_API=https://your-source/board.json
export COMMANDS_API=https://your-source/commands.json
javac -d out src/com/example/KnightBoard.java
java -cp out com.example.KnightBoard
If environment variables aren't set, the program falls back to default test URLs.

✅ Example Output
json
Copy
Edit
{"x":0,"y":0,"direction":"WEST","status":"SUCCESS"}
Possible status values:

SUCCESS

OUT_OF_THE_BOARD

INVALID_START_POSITION

GENERIC_ERROR

🧠 Design Decisions & Thought Process
✅ Simplicity (KISS Principle)
Followed the KISS principle to ensure clarity and maintainability.

Avoided over-engineering — kept everything in a single file with basic classes and logic.

Logic is linear and predictable, aiding debugging and testing.

🔄 Command Strategy
Enforced START as the first command.

Used a simple direction array (NORTH, EAST, SOUTH, WEST) for indexing.

Validated position before every move — stops on obstacles or boundaries.

🧪 Testing Strategy
Prioritized Testing Areas
Start Position Validation

Outside bounds

On an obstacle

Movement

Edge-of-board detection

Obstacle avoidance mid-move

Rotation

Ensure direction changes correctly

Invalid Input

Missing or malformed commands

Techniques
Unit testing with mocked inputs for board and commands

Integration tests using actual API calls (can be faked in dev)

Manual testing for edge and corner positions

🛠️ Technologies Used
Java 17

Jackson for JSON parsing