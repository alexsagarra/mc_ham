(function () {
  var owner = "alexsagarra";
  var repo = "mc_ham";

  var releasesPage = "https://github.com/" + owner + "/" + repo + "/releases";

  var latestBtn = document.getElementById("downloadLatest");
  var releasesBtn = document.getElementById("releasesPage");
  var repoLabel = document.getElementById("repoLabel");

  if (latestBtn) {
    latestBtn.href = releasesPage;
  }

  if (releasesBtn) {
    releasesBtn.href = releasesPage;
  }

  if (repoLabel) {
    repoLabel.textContent = "Repository: " + owner + "/" + repo;
  }
})();
