import "cardsJS";
import "cardsJS/dist/cards.min.css";

console.log("CardJS on window:", (window as any).CardJS);

let socket : WebSocket;

export function log(msg: string) {
  const logEl = document.getElementById("log") as HTMLTextAreaElement;
  logEl.value += msg + "\n";
  logEl.scrollTop = logEl.scrollHeight;
}

export function sendMessage() {
  const value = (document.getElementById("message") as HTMLInputElement).value;
  socket.send(value);
}

export function connect() {
  const url = (document.getElementById("url") as HTMLInputElement).value;
  socket = new WebSocket(url);

  socket.onopen = () => {
    log("Connected to server");
  }
  socket.onmessage = (msg) => {
    receiveData(msg.data)
  }
}


export function disconnect() {
  socket.close();
  (document.getElementById("status") as HTMLLabelElement).innerText = "disconnected";
}

export function receiveData(input: String) {
  const message = input.match(/message=([^,]*)(?:,|$)/)![1];
  log(message);

  const cardsMatch = input.match(/cards=\[([^\]]*)\]/)
  if (!cardsMatch) return;
  const arr:String = cardsMatch?.[0].split("=")[1]!;
  const arr1 = arr.substring(1, arr.length - 1).split(", ")
  console.log(arr1, input)
  const lowerMessage = message.toLowerCase();
  if (lowerMessage.includes("player") && arr1.length > 0){
    const hand = (document.getElementById("player") as HTMLDivElement)
    hand.replaceChildren()
    arr1.forEach((c) => {
      const card = document.createElement("img");
      card.className = "card";
      card.src = `cards/${c}.svg`;
      hand.appendChild(card)
    });

  } else if (lowerMessage.includes("table") && arr1.length > 0){
    const table = (document.getElementById("table") as HTMLDivElement)
    table.replaceChildren()
    arr1.forEach((c) => {
      const card = document.createElement("img");
      card.className = "card";
      card.src = `cards/${c}.svg`;
      table.appendChild(card);
    })
  }
}