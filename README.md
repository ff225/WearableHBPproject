# WearableHB Project

## Descrizione

WearableHB è un'app Android progettata per monitorare e registrare dati vitali come la frequenza
cardiaca, la pressione sanguigna e i dati GPS utilizzando lo smartphone e il Galaxy Watch 5.
L'app raccoglie questi dati tramite Health Connect e li memorizza su un sistema di file distribuito
utilizzando l'API di Pinata.

## Installazione

1. **Requisiti di sistema**:
    - Android 8.0 (Oreo) o superiore
    - Galaxy Watch 5
    - Connessione a Internet

2. **Download**: Scarica l'app
   dal [repository GitHub](https://github.com/ff225/WearableHBPproject/releases).

3. **Installazione**:
    - Abilita l'installazione da fonti sconosciute nelle impostazioni del tuo dispositivo.
    - Installa l'app scaricata.

## Utilizzo

1. **Avvio dell'app**: Dopo aver installato l'app, aprila e concedi tutte le autorizzazioni
   richieste.
2. **Raccolta dati**: L'app inizierà automaticamente a raccogliere dati dal Galaxy Watch 6 e dallo
   smartphone.
3. **Visualizzazione dati**: Puoi visualizzare i dati raccolti direttamente nell'app.
4. **Caricamento dati**: I dati verranno caricati periodicamente su un sistema di file distribuito
   utilizzando l'API di Pinata.

## Funzionalità

- Monitoraggio della frequenza cardiaca dal Galaxy Watch 5.
- Monitoraggio della pressione sanguigna dal Galaxy Watch 5.
- Raccolta di dati GPS dallo smartphone.

## Archiviazione dei dati

I dati vengono memorizzati localmente sullo smartphone in
un [database relazionale (SQLite)](https://github.com/ff225/WearableHBPproject/tree/dev/pinata/app/src/main/java/it/unibo/alessiociarrocchi/tesiahc/data/model)
e successivamente caricati su un sistema di file distribuito
tramite [Pinata API](https://github.com/ff225/WearableHBPproject/blob/dev/pinata/app/src/main/java/it/unibo/alessiociarrocchi/tesiahc/worker/SendDataToIPFS.kt).
