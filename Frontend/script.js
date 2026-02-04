// ==================== TÉMAVÁLTÁS ====================
const currentTheme = localStorage.getItem('theme') || 'light';
if (currentTheme === 'dark') {
    document.body.classList.add('dark-theme');
}

function toggleTheme() {
    document.body.classList.toggle('dark-theme');

    const theme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
    localStorage.setItem('theme', theme);

    const themeIcon = document.querySelector('.theme-icon');
    themeIcon.style.transform = 'rotate(360deg) scale(1.2)';
    setTimeout(() => {
        themeIcon.style.transform = '';
    }, 300);
}


// ==================== SZÁMOLÓGÉP LOGIKA ====================
const display = document.getElementById("addingValue");
const buttons = document.querySelectorAll(".keypad button");

buttons.forEach(button => {
    button.addEventListener("click", () => {

        // DELETE gomb
        if (button.classList.contains("delete")) {
            display.textContent = display.textContent.slice(0, -1) || "0";
            return;
        }

        // CLEAR gomb (C)
        if (button.classList.contains("zeroing")) {
            display.textContent = "0";
            return;
        }

        // SZÁMOK és VESSZŐ
        if (!isNaN(button.textContent) || button.textContent === ',') {
            if (display.textContent === "0" && button.textContent !== ',') {
                display.textContent = button.textContent;
            } else {
                display.textContent += button.textContent;
            }
        }
    });
});


// ==================== KATEGÓRIA KIVÁLASZTÁS ====================
const selectElement = document.querySelector(".cat");

function getSelectedCategory() {
    return selectElement.value;
}

function showToast(message, duration = 3000) {
    const toast = document.createElement('div');
    toast.textContent = message;
    toast.style.cssText = `
    position: fixed;
    bottom: 20px;
    right: 20px;
    background: #333;
    color: white;
    padding: 16px 24px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.3);
    z-index: 9999;
    animation: slideIn 0.3s ease-out;
  `;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}



function showCategories(){
    document.getElementById('categories').classList.add('active');
    document.getElementById('transactions').classList.remove('active');
    document.querySelector('.sumSpending').classList.remove('hide');
    document.querySelector('.transactionsPart').classList.remove('show');
}

function showTransactions(){
    document.getElementById('transactions').classList.add('active');
    document.getElementById('categories').classList.remove('active');
    document.querySelector('.sumSpending').classList.add('hide');
    document.querySelector('.transactionsPart').classList.add('show');
}


// ==================== CONFIRM GOMB (MENTÉS) ====================
const confirmButton = document.querySelector(".confirm");

confirmButton.addEventListener("click", async () => {
    const amount = parseInt(display.textContent.replace(/,/g, ''));  // Vessző eltávolítása
    const category = getSelectedCategory();
    const paymentMethod = document.querySelector(".cash").value;
    const comment = document.querySelector(".comment").value;

    // Validáció
    if (!category || isNaN(amount) || amount <= 0) {
        alert("Hibás adat!");
        return;
    }

    console.log("Küldés:", { category, amount, paymentMethod, comment });

    try {
        const response = await fetch("http://localhost:8080/api/spending", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                category: category,
                amount: amount,
                paymentMethod: paymentMethod,
                comment: comment
            })
        });

        console.log("Response status:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Backend hiba:", errorText);
            throw new Error(`Backend hiba: ${response.status}`);
        }

        const data = await response.json();
        console.log("Backend válasz:", data);

        // UI frissítése
        updateUI(data);

         transactionsUpdate({
        category: category,        // ← lokális változó
         amount: amount       // ← vagy amount (lokális)
    });

        // Mezők visszaállítása
        display.textContent = "0";
        document.querySelector(".comment").value = "";

        showToast("Sikeres mentés!");

    } catch (error) {
        alert("Hiba történt a mentés során!");
        console.error("Fetch hiba:", error);
    }
});



// ==================== UI FRISSÍTÉS ====================
function updateUI(data) {
    console.log("UI frissítés, data:", data);

    // Fő összeg frissítése
    if (data.totalSpending !== undefined) {
        document.querySelector('.spending h2').textContent = data.totalSpending + " Ft";
    }

    // Időintervallumok frissítése
    const timeBoxes = document.querySelectorAll('.time-box');
    if (timeBoxes.length >= 3) {
        if (data.dailySpending !== undefined) {
            timeBoxes[0].querySelector('.time-amount').textContent = data.dailySpending + " Ft";
        }
        if (data.weeklySpending !== undefined) {
            timeBoxes[1].querySelector('.time-amount').textContent = data.weeklySpending + " Ft";
        }
        if (data.monthlySpending !== undefined) {
            timeBoxes[2].querySelector('.time-amount').textContent = data.monthlySpending + " Ft";
        }
    }


    // transactions kiirasa
    
    

    // Kategóriánkénti összegek frissítése
    if (data.summary && Array.isArray(data.summary)) {
        data.summary.forEach(item => {
            const spendingItems = document.querySelectorAll('.spending-item');

            spendingItems.forEach(element => {
                const categoryText = element.querySelector('.category').textContent.trim();

                if (categoryText === item.category) {
                    element.querySelector('.amount').textContent = item.total + " Ft";
                }
            });

            

            


        });

        // Diagram frissítése (százalékok) - JAVÍTOTT VÉGLEGESEN
        const barGroups = document.querySelectorAll('.bar-group');

        data.summary.forEach(item => {
            barGroups.forEach(barGroup => {
                // A kategória neve a .bar-label span-ban van!
                const barLabel = barGroup.querySelector('.bar-label');

                if (barLabel && barLabel.textContent.trim() === item.category) {
                    const percentLabel = barGroup.querySelector('.percent-label');
                    const bar = barGroup.querySelector('.bar');

                    if (percentLabel && bar && item.percentage !== undefined) {
                        const roundedPercent = Math.round(item.percentage);
                        percentLabel.textContent = roundedPercent + '%';
                        bar.style.setProperty('--value', roundedPercent);

                        console.log(`✓ Diagram frissítve: ${item.category} -> ${roundedPercent}%`);
                    }
                }
            });
        });
    }
}

async function loadTransactions(){
    try{
        const response = await fetch("http://localhost:8080/api/transactions");
        if(!response.ok){
            console.error("Nem sikerult a tranzakciok lekerese");
            return;
        }

        const transactions = await response.json();

        transactions.forEach(transactions => {
            transactionsUpdate({
                category : transactions.cateory,
                amount: transactions.amount
            });

        });


    }catch (error){
        console.error("hiba a tranzakciok betoltesekor", error);
    }
}

function transactionsUpdate(transactionData) {
    const transactionsDiv = document.querySelector('.transactions');
    
    const transactionHTML = `
        <div class="transaction-item">
            <span class="category">${transactionData.category}</span>
            <span class="transactionAmount">${transactionData.amount} Ft</span>
        </div>
    `;
    
    transactionsDiv.insertAdjacentHTML('beforeend', transactionHTML);
}

// ==================== OLDAL BETÖLTÉSKOR ====================
window.addEventListener('DOMContentLoaded', async () => {
    try {
        console.log("Statisztikák betöltése...");
        const response = await fetch("http://localhost:8080/api/statistics");

        if (!response.ok) {
            console.error("Nem sikerült a statisztikák lekérése");
            return;
        }

        const data = await response.json();
        console.log("Statisztikák:", data);
        updateUI(data);

        await loadTransactions();
    } catch (error) {
        console.error("Nem sikerült betölteni a statisztikákat:", error);
    }
});