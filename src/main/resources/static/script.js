document.addEventListener('DOMContentLoaded', () => {
    const saveRecord = async (suwayomiId, komgaPath, priority) => {
        if (!suwayomiId || !komgaPath || !priority) {
            alert('All fields are required');
            return false;
        }

        try {
            const response = await fetch('/api/records', {
                method: 'POST',
                body: new URLSearchParams({
                    suwayomiId,
                    komgaPath,
                    priority
                })
            });

            if (response.ok) {
                return true;
            } else {
                const error = await response.text();
                alert('Error: ' + error);
                return false;
            }
        } catch (e) {
            alert('Network error');
            return false;
        }
    };

    document.querySelectorAll('.save-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const row = btn.closest('tr');
            const suwayomiId = row.cells[0].textContent;
            const komgaPath = row.querySelector('.komga-path').value;
            const priority = row.querySelector('.priority').value;

            await saveRecord(suwayomiId, komgaPath, priority);
        });
    });

    document.querySelectorAll('.reset-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            if (!confirm('Reset this record?')) return;

            const row = btn.closest('tr');
            const suwayomiId = row.cells[0].textContent;
            try {
                await fetch(`/api/records/${suwayomiId}`, {
                    method: 'DELETE'
                });

                row.querySelector('.komga-path').value = "";
                row.querySelector('.priority').value = "1";
            } catch (e) {
                alert('Error deleting record');
            }
        });
    });
});