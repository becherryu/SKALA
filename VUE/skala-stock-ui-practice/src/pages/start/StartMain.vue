<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import apiCall from "@/scripts/api-call";
import { storePlayer } from "@/scripts/store-player";
import { notifyInfo, notifyError } from "@/scripts/store-popups";

const playerId = ref("");
const playerPassword = ref("");
const isNewPlayer = ref(false);
const router = useRouter();

const handlePopState = () => {
  history.pushState(null, "", "/start");
};

// 로그인 로직
const login = async () => {
  if (!playerId.value || !playerPassword.value) {
    notifyInfo("아이디와 비밀번호를 입력하세요.");
    return;
  }

  const id = String(playerId.value);
  const password = String(playerPassword.value);

  try {
    // 아이디로 조회
    const res = await apiCall.get("/players", null, {
      playerId: id,
    });

    // 해당하는 player가 id목록에 있는지 조회하여 정보 가져오기
    const player = Array.isArray(res) ? res[0] : null;

    if (!player) {
      notifyInfo("존재하지 않는 아이디입니다. 회원가입을 진행해주세요.");
      isNewPlayer.value = true;
      return;
    }

    if (player.playerPassword !== password) {
      notifyError("아이디 또는 비밀번호가 올바르지 않습니다.");
      return;
    }

    // 로그인 성공
    storePlayer(player);
    router.push("/stock");
  } catch (e) {
    notifyError("로그인 실패");
  }
};

// 회원가입 로직
const signup = async () => {
  if (!playerId.value || !playerPassword.value) {
    notifyError("플레이어ID와 비밀번호를 입력하세요.");
    return;
  }

  const id = String(playerId.value).trim();
  const password = String(playerPassword.value).trim();

  try {
    // 이미 존재하는 플레이어인지 확인
    const checkResponse = await apiCall.get("/players", null, {
      playerId: id,
    });

    if (!checkResponse) {
      notifyInfo("회원가입 확인 중 오류가 발생했습니다.");
      return;
    }

    if (checkResponse[0]) {
      notifyInfo("이미 존재하는 플레이어ID입니다.");
      return;
    }

    // 신규 플레이어 생성
    const createResponse = await apiCall.post("/players", null, {
      playerId: id,
      playerPassword: password,
      playerMoney: 10000,
    });

    console.log(createResponse);

    if (!createResponse) {
      notifyError("회원가입에 실패했습니다.");
      return;
    }

    // 회원가입 성공 → 바로 로그인 처리
    storePlayer(createResponse);
    notifyInfo("회원가입 성공!");
    router.push("/stock");
  } catch (e) {
    console.error(e);
    notifyError("회원가입 실패");
  }
};

// 로그인 이전 단계에서 /start 경로 외에 이동하지 못하도록 막기
onMounted(() => {
  if (router.currentRoute.value.path !== "/start") {
    router.replace("/start");
  }
  history.pushState(null, "", "/start");
  window.addEventListener("popstate", handlePopState);
});

// 컴포넌트 언마운트 시 popstate 이벤트 리스너 제거
onBeforeUnmount(() => {
  window.removeEventListener("popstate", handlePopState);
});
</script>

<template>
  <div
    class="container-sm mt-3 border border-2 p-1 rounded-4"
    style="max-width: 600px"
  >
    <div class="mt-3 d-flex justify-content-center" style="height: 100px">
      <span class="text-center text-black fs-1 fw-bold mt-3"
        >SKALA STOCK Market</span
      >
    </div>
    <div
      class="row bg-secondary-subtle p-4 m-1 rounded-4 gap-2"
      style="opacity: 95%"
    >
      <div class="col gap-2">
        <InlineInput
          label="플레이어ID"
          class="mb-1 rounded-4"
          type="text"
          placeholder="플레이어ID"
          v-model="playerId"
        />
        <InlineInput
          label="비밀번호"
          class="mb-1 rounded-4"
          type="password"
          placeholder="비밀번호"
          v-model="playerPassword"
        />
      </div>
      <div class="d-flex justify-content-end gap-3">
        <button class="btn btn-sm btn-secondary" @click="signup">
          회원가입
        </button>
        <button class="btn btn-sm btn-secondary" @click="login">로그인</button>
      </div>
    </div>
    <div class="bss-background p-1 rounded-4"></div>
  </div>
</template>

<style scoped>
.bss-background {
  width: 590px;
  height: 380px;
  background-image: url("/logo.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}
</style>
