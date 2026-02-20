<script setup>
import { computed } from "vue";
import { Bar } from "vue-chartjs";
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const props = defineProps({
  stats: {
    type: Object,
    required: true,
  },
});

const chartData = computed(() => ({
  labels: Object.keys(props.stats),
  datasets: [
    {
      label: "카테고리별 상품 수",
      data: Object.values(props.stats),
      backgroundColor: "#133763",
      borderRadius: 8,
      barThickness: 48,
    },
  ],
}));

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        stepSize: 1,
      },
    },
  },
};
</script>

<template>
  <div class="chart-container">
    <Bar :data="chartData" :options="options" />
  </div>
</template>

<style scoped>
.chart-container {
  width: 100%;
  height: 100%;
  min-height: 320px;
}
</style>
