package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

internal object ParticipantPdfBuilder {

    fun buildHtml(participants: List<Participant>): String {
        val total     = participants.size
        val validated = participants.count { it.status == ParticipantStatus.VALIDATED }
        val pending   = participants.count { it.status == ParticipantStatus.PENDING }
        val rejected  = participants.count { it.status == ParticipantStatus.REJECTED }
        val rows      = participants.mapIndexed { i, p -> buildRow(i + 1, p) }.joinToString("\n")

        return """<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>My Colo — Participants</title>
<style>
  * { box-sizing: border-box; margin: 0; padding: 0; }
  body { font-family: Arial, sans-serif; padding: 32px; color: #212121; background: #fff; }
  .header { display: flex; align-items: center; gap: 16px; margin-bottom: 28px; padding-bottom: 16px; border-bottom: 3px solid #1565C0; }
  .logo { width: 48px; height: 48px; background: #1565C0; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 900; font-size: 16px; flex-shrink: 0; }
  .header-title h1 { color: #1565C0; font-size: 22px; }
  .header-title p { color: #757575; font-size: 12px; margin-top: 3px; }
  .stats { display: flex; gap: 12px; margin-bottom: 24px; }
  .stat { flex: 1; padding: 14px 16px; border-radius: 10px; }
  .stat-label { font-size: 10px; color: #757575; text-transform: uppercase; letter-spacing: 0.8px; }
  .stat-value { font-size: 30px; font-weight: 900; margin-top: 4px; }
  .s-total     { background:#E3F2FD; } .s-total     .stat-value { color:#1565C0; }
  .s-validated { background:#E8F5E9; } .s-validated .stat-value { color:#2E7D32; }
  .s-pending   { background:#FFF8E1; } .s-pending   .stat-value { color:#F57F17; }
  .s-rejected  { background:#FFEBEE; } .s-rejected  .stat-value { color:#C62828; }
  table { width: 100%; border-collapse: collapse; font-size: 12px; }
  thead tr { background: #1565C0; color: #fff; }
  th { padding: 10px 12px; text-align: left; font-weight: 600; white-space: nowrap; }
  td { padding: 9px 12px; border-bottom: 1px solid #EEEEEE; vertical-align: middle; }
  tr:nth-child(even) td { background: #FAFAFA; }
  .v { color:#2E7D32; font-weight:700; }
  .p { color:#F57F17; font-weight:700; }
  .r { color:#C62828; font-weight:700; }
  .footer { margin-top: 28px; text-align: center; font-size: 11px; color: #9E9E9E; }
  @media print { @page { margin: 16mm; } }
</style>
</head>
<body>
<div class="header">
  <div class="logo">MC</div>
  <div class="header-title">
    <h1>My Colo — Liste des Participants</h1>
    <p>$total participant(s) · Export généré par My Colo Admin</p>
  </div>
</div>
<div class="stats">
  <div class="stat s-total">    <div class="stat-label">Total</div>     <div class="stat-value">$total</div></div>
  <div class="stat s-validated"><div class="stat-label">Validés</div>   <div class="stat-value">$validated</div></div>
  <div class="stat s-pending">  <div class="stat-label">En attente</div><div class="stat-value">$pending</div></div>
  <div class="stat s-rejected"> <div class="stat-label">Rejetés</div>   <div class="stat-value">$rejected</div></div>
</div>
<table>
  <thead>
    <tr>
      <th>#</th>
      <th>Nom complet</th>
      <th>Prénom de famille</th>
      <th>Âge</th>
      <th>Situation</th>
      <th>Statut</th>
      <th>Date d'inscription</th>
    </tr>
  </thead>
  <tbody>
$rows
  </tbody>
</table>
<div class="footer">Powered by DigiPrem · My Colo 2026</div>
</body>
</html>"""
    }

    private fun buildRow(index: Int, p: Participant): String {
        val (statusClass, statusLabel) = when (p.status) {
            ParticipantStatus.VALIDATED -> "v" to "Validé"
            ParticipantStatus.PENDING   -> "p" to "En attente"
            ParticipantStatus.REJECTED  -> "r" to "Rejeté"
        }
        val jobLabel = when (p.jobStatus) {
            JobStatus.STUDENT_SCHOOL -> "Élève"
            JobStatus.STUDENT_HIGHER -> "Étudiant"
            JobStatus.WORKER         -> "Travailleur"
            JobStatus.SEEKING_WORK   -> "Sans emploi"
        }
        return """    <tr><td>$index</td><td>${p.fullName}</td><td>${p.familyName}</td><td>${p.age} ans</td><td>$jobLabel</td><td class="$statusClass">$statusLabel</td><td>${p.registrationDate}</td></tr>"""
    }
}
