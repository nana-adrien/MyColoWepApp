package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

internal object ParticipantPdfBuilder {

    fun buildHtml(participants: List<Participant>): String {
        val total = participants.size
        val rows  = participants.mapIndexed { i, p -> buildRow(i + 1, p) }.joinToString("\n")

        return """<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>My Colo — Participants</title>
<style>
  * { box-sizing: border-box; margin: 0; padding: 0; }
  body { font-family: Arial, sans-serif; padding: 32px; color: #212121; background: #fff; }

  /* ── En-tête ── */
  .header { display: flex; align-items: center; gap: 16px; margin-bottom: 28px; padding-bottom: 16px; border-bottom: 3px solid #1565C0; }
  .logo { width: 48px; height: 48px; background: #1565C0; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 900; font-size: 16px; flex-shrink: 0; }
  .header-title h1 { color: #1565C0; font-size: 22px; }
  .header-title p  { color: #757575; font-size: 12px; margin-top: 3px; }

  /* ── Tableau ── */
  table { width: 100%; border-collapse: collapse; font-size: 12px; }
  thead tr { background: #1565C0; color: #fff; }
  th { padding: 10px 10px; text-align: left; font-weight: 600; white-space: nowrap; }
  td { padding: 8px 10px; border-bottom: 1px solid #EEEEEE; vertical-align: middle; }
  tr:nth-child(even) td { background: #FAFAFA; }
  .num { color: #9E9E9E; font-size: 11px; }

  /* ── Pied de page ── */
  .footer {
    margin-top: 28px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #EEEEEE;
    padding-top: 12px;
    font-size: 10px;
    color: #9E9E9E;
  }
  .footer .site { font-weight: 600; color: #1565C0; }

  @media print {
    @page { margin: 16mm; }
    .footer { position: fixed; bottom: 0; left: 0; right: 0; padding: 8px 32px; background: #fff; }
  }
</style>
</head>
<body>

<!-- En-tête -->
<div class="header">
  <div class="logo">MC</div>
  <div class="header-title">
    <h1>My Colo — Liste des Participants</h1>
    <p>$total participant(s) · Export généré par My Colo Admin</p>
  </div>
</div>

<!-- Tableau participants -->
<table>
  <thead>
    <tr>
      <th>#</th>
      <th>Nom complet</th>
      <th>Prénom de famille</th>
      <th>Date de naissance</th>
      <th>Âge</th>
      <th>Niveau d'étude</th>
      <th>Situation professionnelle</th>
    </tr>
  </thead>
  <tbody>
$rows
  </tbody>
</table>

<!-- Pied de page -->
<div class="footer">
  <span>Powered by <strong>DigiPrem</strong></span>
  <span class="site">mycolo.digiprem.com</span>
  <span id="page-info">Page 1</span>
</div>

<script>
  (function() {
    var pageCount = Math.ceil(document.querySelectorAll('tr[data-row]').length / 30) || 1;
    document.getElementById('page-info').textContent = 'Page 1 / ' + pageCount;
  })();
</script>
</body>
</html>"""
    }

    private fun buildRow(index: Int, p: Participant): String {
        val birthFormatted = "%02d/%02d/%04d".format(
            p.birthDate.dayOfMonth, p.birthDate.monthNumber, p.birthDate.year
        )
        val jobLabel = when (p.jobStatus) {
            JobStatus.STUDENT_SCHOOL -> "Élève"
            JobStatus.STUDENT_HIGHER -> "Étudiant(e)"
            JobStatus.WORKER         -> "Travailleur(se)"
            JobStatus.SEEKING_WORK   -> "Sans emploi"
        }
        val edu = p.educationLevel.ifBlank { "—" }
        return """    <tr data-row="$index"><td class="num">$index</td><td>${p.fullName}</td><td>${p.familyName}</td><td>$birthFormatted</td><td>${p.age} ans</td><td>$edu</td><td>$jobLabel</td></tr>"""
    }
}
