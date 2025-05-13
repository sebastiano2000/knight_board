# Knight Board Simulator

This project simulates a knight’s movement on a 2D board based on a sequence of commands. The knight moves in four directions (`NORTH`, `EAST`, `SOUTH`, `WEST`) while avoiding obstacles and staying within board bounds.

## 🚀 Features

- Parses board dimensions and obstacles from a JSON API
- Processes a sequence of movement and rotation commands
- Handles collision with board edges and obstacles
- Reports final position and status (`SUCCESS`, `OUT_OF_THE_BOARD`, `INVALID_START_POSITION`, or `GENERIC_ERROR`)

## 🧩 Technologies

- Java
- Jackson (for JSON processing)
- HTTPURLConnection (for fetching board and commands)

## 📥 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/knight-board.git
   cd knight-board
