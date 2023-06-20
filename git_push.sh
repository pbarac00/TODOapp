#!/bin/bash

# Unesi poruku za commit kao argument prilikom pokretanja skripte
commit_message=$1

# Provjeri je li unesena poruka za commit
if [ -z "$commit_message" ]; then
  echo "Molimo unesite poruku za commit kao argument prilikom pokretanja skripte."
  exit 1
fi

# Izvrši git add .
git add .

# Izvrši git commit -m "poruka"
git commit -m "$commit_message"

# Izvrši git push
git push
