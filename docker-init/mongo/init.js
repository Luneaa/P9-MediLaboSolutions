db = db.getSiblingDB('medilabo_notes');

db.createCollection('notes');

db.notes.insertMany([
    {
        "idPatient": 1,
        "contenu": "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé",
        "date": new Date()
    },
    {
        "idPatient": 2,
        "contenu": "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement",
        "date": new Date()
    },
    {
        "idPatient": 2,
        "contenu": "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale",
        "date": new Date()
    },
    {
        "idPatient": 3,
        "contenu": "Le patient déclare qu'il fume depuis peu",
        "date": new Date()
    },
    {
        "idPatient": 3,
        "contenu": "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé",
        "date": new Date()
    },
    {
        "idPatient": 4,
        "contenu": "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d'être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments",
        "date": new Date()
    },
    {
        "idPatient": 4,
        "contenu": "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
        "date": new Date()
    },
    {
        "idPatient": 4,
        "contenu": "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé",
        "date": new Date()
    },
    {
        "idPatient": 4,
        "contenu": "Taille, Poids, Cholestérol, Vertige et Réaction",
        "date": new Date()
    }
]);
